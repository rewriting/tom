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
import tom.engine.adt.code.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class KernelTomTyper {
          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) )) )) ;}    







  private SymbolTable symbolTable;

  public KernelTomTyper() {
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
     private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }     

  // ------------------------------------------------------------

  /**
   * If a variable with a type X is found, then all the variables that have the same name and 
   * with type 'unknown' get this type
   *  - apply this for each rhs
   */
  protected Code propagateVariablesTypes(Code workingTerm){
    try{
      return tom_make_TopDown(tom_make_ProcessRhsForVarTypePropagation()).visitLight(workingTerm);  
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("propagateVariablesTypes: failure on " + workingTerm);
    }
  }
  public static class ProcessRhsForVarTypePropagation extends tom.library.sl.AbstractStrategyBasic {public ProcessRhsForVarTypePropagation() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {


        HashMap<String,TomType> varTypes = new HashMap<String,TomType>();
        tom_make_TopDown(tom_make_CollectAllVariablesTypes(varTypes)).visitLight( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getConstraint() );        
        return tom_make_TopDown(tom_make_PropagateVariablesTypes(varTypes)).visitLight((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg));        
      }}}}return _visit_ConstraintInstruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  _visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.ConstraintInstruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {return ((T)visit_ConstraintInstruction((( tom.engine.adt.tominstruction.types.ConstraintInstruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_ProcessRhsForVarTypePropagation() { return new ProcessRhsForVarTypePropagation();}public static class CollectAllVariablesTypes extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashMap  map;public CollectAllVariablesTypes( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch241_9= false ; tom.engine.adt.tomname.types.TomName  tomMatch241_2= null ; tom.engine.adt.tomtype.types.TomType  tomMatch241_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch241_9= true ;tomMatch241_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;tomMatch241_3= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch241_9= true ;tomMatch241_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;tomMatch241_3= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;}}}if (tomMatch241_9) {if ( (tomMatch241_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomtype.types.TomType  tom_type=tomMatch241_3;if ( (tom_type instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch241_8= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom_type) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {tomMatch241_8= true ;}if (!(tomMatch241_8)) {






        if(tom_type!=SymbolTable.TYPE_UNKNOWN) {
          map.put( tomMatch241_2.getString() ,tom_type);
        }
      }}}}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectAllVariablesTypes( java.util.HashMap  t0) { return new CollectAllVariablesTypes(t0);}public static class PropagateVariablesTypes extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashMap  map;public PropagateVariablesTypes( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch242_6= false ; tom.engine.adt.tomtype.types.TomType  tomMatch242_2= null ; tom.engine.adt.tomname.types.TomName  tomMatch242_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch242_6= true ;tomMatch242_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;tomMatch242_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch242_6= true ;tomMatch242_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;tomMatch242_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;}}}if (tomMatch242_6) {if ( (tomMatch242_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch242_1.getString() ; tom.engine.adt.tomtype.types.TomType  tom_type=tomMatch242_2;






        if(tom_type==SymbolTable.TYPE_UNKNOWN || tom_type.isEmptyType()) {
          if (map.containsKey(tom_name)) {
            return (( tom.engine.adt.code.types.BQTerm )tom__arg).setAstType((TomType)map.get(tom_name)); 
          }
        }
      }}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch243_6= false ; tom.engine.adt.tomtype.types.TomType  tomMatch243_2= null ; tom.engine.adt.tomname.types.TomName  tomMatch243_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch243_6= true ;tomMatch243_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;tomMatch243_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch243_6= true ;tomMatch243_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;tomMatch243_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;}}}if (tomMatch243_6) {if ( (tomMatch243_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch243_1.getString() ; tom.engine.adt.tomtype.types.TomType  tom_type=tomMatch243_2;



        if(tom_type==SymbolTable.TYPE_UNKNOWN || tom_type.isEmptyType()) {
          if (map.containsKey(tom_name)) {
            return (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).setAstType((TomType)map.get(tom_name)); 
          }
        }
      }}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_PropagateVariablesTypes( java.util.HashMap  t0) { return new PropagateVariablesTypes(t0);}



  /*
   * The "typeVariable" phase types RecordAppl into Variable
   * we focus on
   * - Match
   *
   * The types of subjects are inferred from the patterns
   *
   * Variable and TermAppl are typed in the TomTerm case
   */

  public <T extends tom.library.sl.Visitable> T typeVariable(TomType contextType, T subject) {
    if(contextType == null) {
      throw new TomRuntimeException("typeVariable: null contextType");
    }
    try {
      //System.out.println("typeVariable: " + contextType);
      //System.out.println("typeVariable subject: " + subject);
      T res = tom_make_TopDownStopOnSuccess(tom_make_replace_typeVariable(contextType,this)).visitLight(subject);
      //System.out.println("res: " + res);
      return res;
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeVariable: failure on " + subject);
    }
  }

  public static class replace_typeVariable extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  contextType;private  KernelTomTyper  kernelTomTyper;public replace_typeVariable( tom.engine.adt.tomtype.types.TomType  contextType,  KernelTomTyper  kernelTomTyper) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.kernelTomTyper=kernelTomTyper;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  KernelTomTyper  getkernelTomTyper() {return kernelTomTyper;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.Option  visit_Option( tom.engine.adt.tomoption.types.Option  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tom__arg) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {


 return (( tom.engine.adt.tomoption.types.Option )tom__arg); }}}}return _visit_Option(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TargetLanguage  visit_TargetLanguage( tom.engine.adt.tomsignature.types.TargetLanguage  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.tomsignature.types.targetlanguage.TL) ) {



 return (( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg); }}}{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.tomsignature.types.targetlanguage.ITL) ) {
 return (( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg); }}}{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.tomsignature.types.targetlanguage.Comment) ) {
 return (( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg); }}}}return _visit_TargetLanguage(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {




        TomType type = kernelTomTyper.getType( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() );
        if(type != null) {
          return type;
        } else {
          return (( tom.engine.adt.tomtype.types.TomType )tom__arg); // useful for SymbolTable.TYPE_UNKNOWN
        }
      }}}}}return _visit_TomType(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tom__arg) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getAstConstraintInstructionList() ;




        TomType newType = kernelTomTyper.typeVariable(contextType, (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getVNode() );
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        tom_make_TopDownCollect(tom_make_CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(tom_constraintInstructionList);
        return  tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make(newType, kernelTomTyper.typeConstraintInstructionList(newType,tom_constraintInstructionList,matchAndNumericConstraints),  (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getOption() ) ;
      }}}}return _visit_TomVisit(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() ;









        TomType newType = contextType;
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        tom_make_TopDownCollect(tom_make_CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(tom_constraintInstructionList);
        return  tom.engine.adt.tominstruction.types.instruction.Match.make(kernelTomTyper.typeConstraintInstructionList(newType,tom_constraintInstructionList,matchAndNumericConstraints),  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOption() ) ;
      }}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch249_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_option= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() ;if ( ((tomMatch249_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch249_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList=tomMatch249_2;if (!( tomMatch249_2.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch249_10= tomMatch249_2.getHeadconcTomName() ;if ( (tomMatch249_10 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch249_10.getString() ; tom.engine.adt.tomslot.types.SlotList  tom_slotList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlots() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraints= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;




        TomSymbol tomSymbol = null;
        if(tom_tomName.equals("")) {
          try {
            tomSymbol = kernelTomTyper.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            tom_nameList=  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(tomSymbol.getAstName(), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ;
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = kernelTomTyper.getSymbolFromName(tom_tomName);
        }

        if(tomSymbol != null) {
          SlotList subterm = kernelTomTyper.typeVariableList(tomSymbol, tom_slotList);
          ConstraintList newConstraints = kernelTomTyper.typeVariable(TomBase.getSymbolCodomain(tomSymbol),tom_constraints);
          return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_option, tom_nameList, subterm, newConstraints) ;
        } else {
          //System.out.println("contextType = " + contextType);

          {{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch250_2= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch250_2= true ;} else {if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {tomMatch250_2= true ;}}if (tomMatch250_2) {

              SlotList subterm = kernelTomTyper.typeVariableList( tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol.make() , tom_slotList);
              ConstraintList newConstraints = kernelTomTyper.typeVariable((( tom.engine.adt.tomtype.types.TomType )contextType),tom_constraints);
              return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_option, tom_nameList, subterm, newConstraints) ;
            }}}{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {


              // do nothing
              //System.out.println("contextType = " + contextType);
              //System.out.println("subject        = " + subject);
            }}}

        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch249_19= false ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch249_13= null ; tom.engine.adt.tomtype.types.TomType  tomMatch249_12= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch249_19= true ;tomMatch249_12= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;tomMatch249_13= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {{tomMatch249_19= true ;tomMatch249_12= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;tomMatch249_13= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;}}}if (tomMatch249_19) {if ( (tomMatch249_12 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( tomMatch249_12.getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) { tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);


        TomType localType = kernelTomTyper.getType( tomMatch249_12.getTomType() );
        //System.out.println("localType = " + localType);
        if(localType != null) {
          // The variable has already a known type
          return tom_var.setAstType(localType);
        }

        //System.out.println("contextType = " + contextType);
        {{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch251_4= false ; String  tomMatch251_1= "" ; tom.engine.adt.tomtype.types.TomType  tomMatch251_2= null ;if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch251_4= true ;tomMatch251_1= (( tom.engine.adt.tomtype.types.TomType )contextType).getTomType() ;tomMatch251_2= (( tom.engine.adt.tomtype.types.TomType )contextType).getTlType() ;}} else {if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch251_4= true ;tomMatch251_1= (( tom.engine.adt.tomtype.types.TomType )contextType).getTomType() ;tomMatch251_2= (( tom.engine.adt.tomtype.types.TomType )contextType).getTlType() ;}}}if (tomMatch251_4) {

            TomType ctype =  tom.engine.adt.tomtype.types.tomtype.Type.make(tomMatch251_1, tomMatch251_2) ;
            ConstraintList newConstraints = kernelTomTyper.typeVariable(ctype,tomMatch249_13);
            TomTerm newVar = tom_var.setAstType(ctype);
            //System.out.println("newVar = " + newVar);
            return newVar.setConstraints(newConstraints);
          }}}}

      }}}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch252_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_option= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOption() ;if ( (tomMatch252_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch252_2.getString() ; tom.engine.adt.tomname.types.TomName  tom_name=tomMatch252_2; tom.engine.adt.code.types.BQTermList  tom_args= (( tom.engine.adt.code.types.BQTerm )tom__arg).getArgs() ;





        TomSymbol tomSymbol = null;
        if(tom_tomName.equals("")) {
          try {
            tomSymbol = kernelTomTyper.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            tom_name= tomSymbol.getAstName();
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = kernelTomTyper.getSymbolFromName(tom_tomName);
        }

        if(tomSymbol != null) {
          BQTermList subterm = kernelTomTyper.typeVariableList(tomSymbol, tom_args);
          return  tom.engine.adt.code.types.bqterm.BQAppl.make(tom_option, tom_name, subterm) ;
        } else {
          //System.out.println("contextType = " + contextType);

          {{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch253_2= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch253_2= true ;} else {if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {tomMatch253_2= true ;}}if (tomMatch253_2) {

              BQTermList subterm = kernelTomTyper.typeVariableList( tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol.make() , tom_args);
              return  tom.engine.adt.code.types.bqterm.BQAppl.make(tom_option, tom_name, subterm) ;
            }}}{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {


              // do nothing
              //System.out.println("contextType = " + contextType);
              //System.out.println("subject        = " + subject);
            }}}

        }
      }}}}{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch252_8= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;if ( (tomMatch252_8 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( tomMatch252_8.getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) { tom.engine.adt.code.types.BQTerm  tom_var=(( tom.engine.adt.code.types.BQTerm )tom__arg);


        TomType localType = kernelTomTyper.getType( tomMatch252_8.getTomType() );
        //System.out.println("localType = " + localType);
        if(localType != null) {
          // The variable has already a known type
          return tom_var.setAstType(localType);
        }

        //System.out.println("contextType = " + contextType);
        {{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch254_4= false ; String  tomMatch254_1= "" ; tom.engine.adt.tomtype.types.TomType  tomMatch254_2= null ;if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch254_4= true ;tomMatch254_1= (( tom.engine.adt.tomtype.types.TomType )contextType).getTomType() ;tomMatch254_2= (( tom.engine.adt.tomtype.types.TomType )contextType).getTlType() ;}} else {if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch254_4= true ;tomMatch254_1= (( tom.engine.adt.tomtype.types.TomType )contextType).getTomType() ;tomMatch254_2= (( tom.engine.adt.tomtype.types.TomType )contextType).getTlType() ;}}}if (tomMatch254_4) {

            TomType ctype =  tom.engine.adt.tomtype.types.tomtype.Type.make(tomMatch254_1, tomMatch254_2) ;
            return tom_var.setAstType(ctype);
          }}}}

      }}}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TargetLanguage  _visit_TargetLanguage( tom.engine.adt.tomsignature.types.TargetLanguage  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomsignature.types.TargetLanguage )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.Option  _visit_Option( tom.engine.adt.tomoption.types.Option  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomoption.types.Option )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {return ((T)visit_TargetLanguage((( tom.engine.adt.tomsignature.types.TargetLanguage )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomoption.types.Option) ) {return ((T)visit_Option((( tom.engine.adt.tomoption.types.Option )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_replace_typeVariable( tom.engine.adt.tomtype.types.TomType  t0,  KernelTomTyper  t1) { return new replace_typeVariable(t0,t1);}



  /*
   *
   * @param contextType
   * @param constraintInstructionList a list of ConstraintInstruction
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   */
  private ConstraintInstructionList typeConstraintInstructionList(TomType contextType, ConstraintInstructionList constraintInstructionList, Collection<Constraint> matchAndNumericConstraints) {
    {{if ( (constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {if ( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).isEmptyconcConstraintInstruction() ) {

        return constraintInstructionList; 
      }}}}{if ( (constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {if (!( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch255_9= (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).getHeadconcConstraintInstruction() ;if ( (tomMatch255_9 instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {

 
        try {
          Collection<TomTerm> lhsVariable = new HashSet<TomTerm>();
          Constraint newConstraint = tom_make_TopDownStopOnSuccess(tom_make_typeConstraint(contextType,lhsVariable,matchAndNumericConstraints,this)).visitLight( tomMatch255_9.getConstraint() );
          TomList varList = ASTFactory.makeTomList(lhsVariable);
          Instruction newAction = (Instruction) replaceInstantiatedVariable(varList, tomMatch255_9.getAction() );
          newAction = typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,newAction);
          ConstraintInstructionList newTail = typeConstraintInstructionList(contextType, (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).getTailconcConstraintInstruction() ,matchAndNumericConstraints);
          return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(newConstraint, newAction,  tomMatch255_9.getOption() ) ,tom_append_list_concConstraintInstruction(newTail, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
        } catch(VisitFailure e) {}
      }}}}}}
        
    throw new TomRuntimeException("Bad ConstraintInstruction: " + constraintInstructionList);
  }

  /**
   * Try to guess the type for the subjects
   * @param contextType the context in which the constraint is typed
   * @param lhsVariable (computed by this strategy) the list of variables that occur in all the lhs 
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   * @param kernelTomTyper the current class
   */
  public static class typeConstraint extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  contextType;private  java.util.Collection  lhsVariable;private  java.util.Collection  matchAndNumericConstraints;private  KernelTomTyper  kernelTomTyper;public typeConstraint( tom.engine.adt.tomtype.types.TomType  contextType,  java.util.Collection  lhsVariable,  java.util.Collection  matchAndNumericConstraints,  KernelTomTyper  kernelTomTyper) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.lhsVariable=lhsVariable;this.matchAndNumericConstraints=matchAndNumericConstraints;this.kernelTomTyper=kernelTomTyper;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  java.util.Collection  getlhsVariable() {return lhsVariable;}public  java.util.Collection  getmatchAndNumericConstraints() {return matchAndNumericConstraints;}public  KernelTomTyper  getkernelTomTyper() {return kernelTomTyper;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.code.types.BQTerm  tom_subject= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() ;



        BQTerm newSubject = tom_subject;
        TomType newSubjectType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
        {{if ( (tom_subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch257_7= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch257_1= null ; tom.engine.adt.tomtype.types.TomType  tomMatch257_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch257_2= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch257_7= true ;tomMatch257_1= (( tom.engine.adt.code.types.BQTerm )tom_subject).getOption() ;tomMatch257_2= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstName() ;tomMatch257_3= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch257_7= true ;tomMatch257_1= (( tom.engine.adt.code.types.BQTerm )tom_subject).getOption() ;tomMatch257_2= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstName() ;tomMatch257_3= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstType() ;}}}if (tomMatch257_7) {if ( (tomMatch257_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

            BQTerm newVariable = null;
            // tomType may be a TomTypeAlone or a type from an typed variable
            String type = TomBase.getTomType(tomMatch257_3);
            //System.out.println("match type = " + type);
            if(kernelTomTyper.getType(type) == null) {
              /* the subject is a variable with an unknown type */
              newSubjectType = kernelTomTyper.guessSubjectType(tom_subject,matchAndNumericConstraints);
              if(newSubjectType != null) {
                newVariable =  tom.engine.adt.code.types.bqterm.BQVariable.make(tomMatch257_1, tomMatch257_2, newSubjectType) ;
              } else {
                  throw new TomRuntimeException("No symbol found for name '" +  tomMatch257_2.getString() + "'");
              }
            } else {
              newVariable = tom_subject;
            }
            if(newVariable == null) {
                throw new TomRuntimeException("Type cannot be guessed for '" + tom_subject+ "'");
            } else {
              newSubject = newVariable;
              newSubjectType = newVariable.getAstType();
            }                  
          }}}}{if ( (tom_subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch257_9= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstName() ;if ( (tomMatch257_9 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch257_9.getString() ;


            TomSymbol symbol = kernelTomTyper.getSymbolFromName(tom_name);
            TomType type = null;
            if(symbol!=null) {
              type = TomBase.getSymbolCodomain(symbol);
              if(type != null) {
                newSubject = (( tom.engine.adt.code.types.BQTerm )tom_subject);
              }
            } else {
              // unknown function call
              type = kernelTomTyper.guessSubjectType(tom_subject,matchAndNumericConstraints);
              if(type != null) {
                newSubject =  tom.engine.adt.code.types.bqterm.FunctionCall.make(tomMatch257_9, type,  (( tom.engine.adt.code.types.BQTerm )tom_subject).getArgs() ) ;
              }
            }
            if (type == null) {
              throw new TomRuntimeException("No symbol found for name '" + tom_name+ "'");
            } else {
              newSubjectType = type;
            }                   
          }}}}{if ( (tom_subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.bqterm.BuildReducedTerm) ) {


            
            newSubjectType =  (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstType() ;
            newSubject = (( tom.engine.adt.code.types.BQTerm )tom_subject);
          }}}{if ( (tom_subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch257_18= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstName() ;if ( (tomMatch257_18 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


            newSubjectType = TomBase.getSymbolCodomain(kernelTomTyper.getSymbolTable().getSymbolFromName( tomMatch257_18.getString() ));
            newSubject = (( tom.engine.adt.code.types.BQTerm )tom_subject);
         }}}}}
 // end match subject     
        
        newSubjectType = kernelTomTyper.typeVariable(contextType,newSubjectType);
        newSubject = kernelTomTyper.typeVariable(newSubjectType, newSubject);                  
        TomTerm newPattern = kernelTomTyper.typeVariable(newSubjectType,  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() );
        TomBase.collectVariable(lhsVariable,newPattern);
        return (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).setPattern(newPattern).setSubject(newSubject);               
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        // if it is numeric, we do not care about the type
        BQTerm newLhs = kernelTomTyper.typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getLeft() );                  
        BQTerm newRhs = kernelTomTyper.typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getRight() );                  
        return (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).setLeft(newLhs).setRight(newRhs);               
      }}}}return _visit_Constraint(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_typeConstraint( tom.engine.adt.tomtype.types.TomType  t0,  java.util.Collection  t1,  java.util.Collection  t2,  KernelTomTyper  t3) { return new typeConstraint(t0,t1,t2,t3);}



  private TomType guessSubjectType(BQTerm subject, Collection matchConstraints) {
    for(Object constr:matchConstraints) {
      {{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() ; tom.engine.adt.code.types.BQTerm  tom_s= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ;

          // we want two terms to be equal even if their option is different 
          // ( because of their possition for example )
matchL:  {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {if ( (tom_s instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_s) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() == (( tom.engine.adt.code.types.BQTerm )tom_s).getAstName() ) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstType() == (( tom.engine.adt.code.types.BQTerm )tom_s).getAstType() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {if ( (tom_s instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_s) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() == (( tom.engine.adt.code.types.BQTerm )tom_s).getAstName() ) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getArgs() == (( tom.engine.adt.code.types.BQTerm )tom_s).getArgs() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildReducedTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch259_22= (( tom.engine.adt.code.types.BQTerm )subject).getTomTerm() ;if ( (tomMatch259_22 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( (tom_s instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_s) instanceof tom.engine.adt.code.types.bqterm.BuildReducedTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch259_28= (( tom.engine.adt.code.types.BQTerm )tom_s).getTomTerm() ;if ( (tomMatch259_28 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( ( tomMatch259_22.getNameList() == tomMatch259_28.getNameList() ) ) {if ( ( tomMatch259_22.getArgs() == tomMatch259_28.getArgs() ) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstType() == (( tom.engine.adt.code.types.BQTerm )tom_s).getAstType() ) ) {
break matchL;}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (tom_s instanceof tom.engine.adt.code.types.BQTerm) ) {
 continue; }}}}

         TomTerm patt = tom_pattern;
         {{if ( (tom_pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_pattern) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 patt =  (( tom.engine.adt.tomterm.types.TomTerm )tom_pattern).getTomTerm() ; }}}}{{if ( (patt instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch261_8= false ; tom.engine.adt.tomname.types.TomNameList  tomMatch261_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )patt) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch261_8= true ;tomMatch261_1= (( tom.engine.adt.tomterm.types.TomTerm )patt).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )patt) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch261_8= true ;tomMatch261_1= (( tom.engine.adt.tomterm.types.TomTerm )patt).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )patt) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {{tomMatch261_8= true ;tomMatch261_1= (( tom.engine.adt.tomterm.types.TomTerm )patt).getNameList() ;}}}}if (tomMatch261_8) {if ( ((tomMatch261_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch261_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch261_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch261_7= tomMatch261_1.getHeadconcTomName() ;if ( (tomMatch261_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        
             TomSymbol symbol = null;
             symbol = getSymbolFromName( tomMatch261_7.getString() );
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
  private BQTermList typeVariableList(TomSymbol symbol, BQTermList subtermList) {
    if(symbol == null) {
      throw new TomRuntimeException("typeVariableList: null symbol");
    }

    if(subtermList.isEmptyconcBQTerm()) {
      return  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    }

    //System.out.println("symbol = " + symbol.getastname());
    {{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol) ) {if ( (subtermList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )subtermList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )subtermList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {if (!( (( tom.engine.adt.code.types.BQTermList )subtermList).isEmptyconcBQTerm() )) {

        /*
         * if the top symbol is unknown, the subterms
         * are typed in an empty context
         */
        BQTermList sl = typeVariableList((( tom.engine.adt.tomsignature.types.TomSymbol )symbol), (( tom.engine.adt.code.types.BQTermList )subtermList).getTailconcBQTerm() );
        return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , (( tom.engine.adt.code.types.BQTermList )subtermList).getHeadconcBQTerm() ),tom_append_list_concBQTerm(sl, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
      }}}}}}{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch262_9= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getTypesToType() ;if ( (tomMatch262_9 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch262_12= tomMatch262_9.getCodomain() ;if ( (tomMatch262_12 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomsignature.types.TomSymbol  tom_symb=(( tom.engine.adt.tomsignature.types.TomSymbol )symbol);if ( (subtermList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )subtermList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )subtermList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {if (!( (( tom.engine.adt.code.types.BQTermList )subtermList).isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tom_head= (( tom.engine.adt.code.types.BQTermList )subtermList).getHeadconcBQTerm() ; tom.engine.adt.code.types.BQTermList  tom_tail= (( tom.engine.adt.code.types.BQTermList )subtermList).getTailconcBQTerm() ;



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

            {{if ( (tom_head instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_head) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

                BQTermList sl = typeVariableList(tom_symb,tom_tail);
                return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.BQVariableStar.make( (( tom.engine.adt.code.types.BQTerm )tom_head).getOption() ,  (( tom.engine.adt.code.types.BQTerm )tom_head).getAstName() ,  tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch262_12.getTomType() ,  tomMatch262_12.getTlType() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getAstName() ) ) ,tom_append_list_concBQTerm(sl, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
              }}}{if ( (tom_head instanceof tom.engine.adt.code.types.BQTerm) ) {

                //we cannot know the type precisely (the var can be of domain or codomain type)
                BQTermList sl = typeVariableList(tom_symb,tom_tail);
                return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,tom_head),tom_append_list_concBQTerm(sl, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
              }}}

          } else {
            BQTermList sl = typeVariableList(tom_symb,tom_tail);
            //TODO: find the correct type of this argument (using its rank)
            return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,tom_head),tom_append_list_concBQTerm(sl, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
          }
        }}}}}}}}}

    throw new TomRuntimeException("typeVariableList: strange case: '" + symbol + "'");
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
    {{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol) ) {if ( (subtermList instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( (( tom.engine.adt.tomslot.types.SlotList )subtermList).isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch264_8= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getHeadconcSlot() ;if ( (tomMatch264_8 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {

        /*
         * if the top symbol is unknown, the subterms
         * are typed in an empty context
         */
        SlotList sl = typeVariableList((( tom.engine.adt.tomsignature.types.TomSymbol )symbol), (( tom.engine.adt.tomslot.types.SlotList )subtermList).getTailconcSlot() );
        return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tomMatch264_8.getSlotName() , typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , tomMatch264_8.getAppl() )) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
      }}}}}}}{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch264_12= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getTypesToType() ;if ( (tomMatch264_12 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch264_15= tomMatch264_12.getCodomain() ;if ( (tomMatch264_15 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tom_codomain=tomMatch264_15; tom.engine.adt.tomsignature.types.TomSymbol  tom_symb=(( tom.engine.adt.tomsignature.types.TomSymbol )symbol);if ( (subtermList instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( (( tom.engine.adt.tomslot.types.SlotList )subtermList).isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch264_25= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getHeadconcSlot() ;if ( (tomMatch264_25 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomname.types.TomName  tom_slotName= tomMatch264_25.getSlotName() ; tom.engine.adt.tomterm.types.TomTerm  tom_slotAppl= tomMatch264_25.getAppl() ; tom.engine.adt.tomslot.types.SlotList  tom_tail= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getTailconcSlot() ;



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

                ConstraintList newconstraints = typeVariable(tom_codomain, (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getConstraints() );
                SlotList sl = typeVariableList(tom_symb,tom_tail);
                return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName,  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getOption() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getAstName() ,  tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch264_15.getTomType() ,  tomMatch264_15.getTlType() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getAstName() ) , newconstraints) ) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
              }}}{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {


                ConstraintList newconstraints = typeVariable(tom_codomain, (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getConstraints() );
                SlotList sl = typeVariableList(tom_symb,tom_tail);
                return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName,  tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getOption() , tom_codomain, newconstraints) ) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
              }}}{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {


                TomType domaintype =  tomMatch264_12.getDomain() .getHeadconcTomType();
                SlotList sl = typeVariableList(tom_symb,tom_tail);
                SlotList res =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName, typeVariable(domaintype,tom_slotAppl)) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
                //System.out.println("domaintype = " + domaintype);
                //System.out.println("res = " + res);
                return res;

              }}}

          } else {
            SlotList sl = typeVariableList(tom_symb,tom_tail);
            return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName, typeVariable(TomBase.getSlotType(tom_symb,tom_slotName),tom_slotAppl)) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
          }
        }}}}}}}}}}

    throw new TomRuntimeException("typeVariableList: strange case: '" + symbol + "'");
  }

  public static class replace_replaceInstantiatedVariable extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomterm.types.TomList  instantiatedVariable;public replace_replaceInstantiatedVariable( tom.engine.adt.tomterm.types.TomList  instantiatedVariable) {super(( new tom.library.sl.Fail() ));this.instantiatedVariable=instantiatedVariable;}public  tom.engine.adt.tomterm.types.TomList  getinstantiatedVariable() {return instantiatedVariable;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tom_subject=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);{{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch267_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getNameList() ; tom.engine.adt.tomslot.types.SlotList  tomMatch267_3= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getSlots() ;if ( ((tomMatch267_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch267_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch267_2.isEmptyconcTomName() )) {if (  tomMatch267_2.getTailconcTomName() .isEmptyconcTomName() ) {if ( ((tomMatch267_3 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch267_3 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if ( tomMatch267_3.isEmptyconcSlot() ) {if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch267__end__11=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch267__end__11.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch267_15= tomMatch267__end__11.getHeadconcTomTerm() ;boolean tomMatch267_17= false ; tom.engine.adt.tomname.types.TomName  tomMatch267_14= null ;if ( (tomMatch267_15 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch267_17= true ;tomMatch267_14= tomMatch267_15.getAstName() ;}} else {if ( (tomMatch267_15 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch267_17= true ;tomMatch267_14= tomMatch267_15.getAstName() ;}}}if (tomMatch267_17) {if ( ( tomMatch267_2.getHeadconcTomName() ==tomMatch267_14) ) {




            return  tomMatch267__end__11.getHeadconcTomTerm() ;
          }}}if ( tomMatch267__end__11.isEmptyconcTomTerm() ) {tomMatch267__end__11=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch267__end__11= tomMatch267__end__11.getTailconcTomTerm() ;}}} while(!( (tomMatch267__end__11==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}}}}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch267__end__25=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch267__end__25.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch267_29= tomMatch267__end__25.getHeadconcTomTerm() ;boolean tomMatch267_31= false ; tom.engine.adt.tomname.types.TomName  tomMatch267_28= null ;if ( (tomMatch267_29 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch267_31= true ;tomMatch267_28= tomMatch267_29.getAstName() ;}} else {if ( (tomMatch267_29 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch267_31= true ;tomMatch267_28= tomMatch267_29.getAstName() ;}}}if (tomMatch267_31) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ==tomMatch267_28) ) {

            return  tomMatch267__end__25.getHeadconcTomTerm() ;
          }}}if ( tomMatch267__end__25.isEmptyconcTomTerm() ) {tomMatch267__end__25=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch267__end__25= tomMatch267__end__25.getTailconcTomTerm() ;}}} while(!( (tomMatch267__end__25==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch267__end__39=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch267__end__39.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch267_43= tomMatch267__end__39.getHeadconcTomTerm() ;if ( (tomMatch267_43 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() == tomMatch267_43.getAstName() ) ) {

            return  tomMatch267__end__39.getHeadconcTomTerm() ;
          }}}if ( tomMatch267__end__39.isEmptyconcTomTerm() ) {tomMatch267__end__39=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch267__end__39= tomMatch267__end__39.getTailconcTomTerm() ;}}} while(!( (tomMatch267__end__39==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}}

      }}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_replace_replaceInstantiatedVariable( tom.engine.adt.tomterm.types.TomList  t0) { return new replace_replaceInstantiatedVariable(t0);}



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
  public static class CollectMatchAndNumericConstraints extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  constrList;public CollectMatchAndNumericConstraints( java.util.Collection  constrList) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;}public  java.util.Collection  getconstrList() {return constrList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {boolean tomMatch268_2= false ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {tomMatch268_2= true ;} else {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {tomMatch268_2= true ;}}if (tomMatch268_2) {

        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));
        throw new VisitFailure();// to stop the top-down
      }}}}return _visit_Constraint(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectMatchAndNumericConstraints( java.util.Collection  t0) { return new CollectMatchAndNumericConstraints(t0);}


}

