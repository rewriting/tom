/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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
 * Radu Kopetz e-mail: Radu.Kopetz@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.TomBase;
import tom.engine.compiler.Compiler;

/**
 * Syntactic propagator
 */
public class ArrayPropagator implements IBasePropagator {

//--------------------------------------------------------	
  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */  /* Generated by TOM (version 2.6alpha): Do not edit this file */    private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;     }   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if( (( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ).isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getHeadAndConstraint() :l1),l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getHeadAndConstraint() :l1),tom_append_list_AndConstraint((( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;     }   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;     }   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;     }   }    /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )) == null )) {         return ( (l2==null)?((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1):new tom.library.sl.Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1),l2) );       } else {         return ( (tom_append_list_Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),l2)==null)?((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1):new tom.library.sl.Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1),tom_append_list_Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else {       return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );     }   }    /* Generated by TOM (version 2.6alpha): Do not edit this file */ /* Generated by TOM (version 2.6alpha): Do not edit this file */private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) ); }   

//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return (Constraint)tom_make_TopDown(tom_make_ArrayPatternMatching()).visitLight(constraint);		
  }	

  private static class ArrayPatternMatching extends  tom.engine.adt.tomsignature.TomSignatureBasicStrategy  {public ArrayPatternMatching() { super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() { return 1; }public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {{  tom.engine.adt.tomconstraint.types.Constraint  tomMatch163NameNumberfreshSubject_1=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);if ( (tomMatch163NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch163NameNumber_freshVar_0= tomMatch163NameNumberfreshSubject_1.getpattern() ;{  tom.engine.adt.tomterm.types.TomTerm  tomMatch163NameNumber_freshVar_1= tomMatch163NameNumberfreshSubject_1.getsubject() ;if ( (tomMatch163NameNumber_freshVar_0 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{  tom.engine.adt.tomname.types.TomNameList  tomMatch163NameNumber_freshVar_2= tomMatch163NameNumber_freshVar_0.getNameList() ;{  tom.engine.adt.tomslot.types.SlotList  tomMatch163NameNumber_freshVar_3= tomMatch163NameNumber_freshVar_0.getSlots() ;if ( ((tomMatch163NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch163NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {{  tom.engine.adt.tomname.types.TomNameList  tomMatch163NameNumber_freshVar_4=tomMatch163NameNumber_freshVar_2;if (!( tomMatch163NameNumber_freshVar_4.isEmptyconcTomName() )) {if ( ( tomMatch163NameNumber_freshVar_4.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {{  String  tomMatch163NameNumber_freshVar_6=  tomMatch163NameNumber_freshVar_4.getHeadconcTomName() .getString() ;{  tom.engine.adt.tomname.types.TomNameList  tomMatch163NameNumber_freshVar_5= tomMatch163NameNumber_freshVar_4.getTailconcTomName() ;if ( tomMatch163NameNumber_freshVar_5.isEmptyconcTomName() ) {{  tom.engine.adt.tomconstraint.types.Constraint  tom_m=tomMatch163NameNumberfreshSubject_1;{ boolean tomMatch163NameNumber_freshVar_8= false ;if ( ((tomMatch163NameNumber_freshVar_3 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch163NameNumber_freshVar_3 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {{  tom.engine.adt.tomslot.types.SlotList  tomMatch163NameNumber_freshVar_7=tomMatch163NameNumber_freshVar_3;if ( tomMatch163NameNumber_freshVar_7.isEmptyconcSlot() ) {tomMatch163NameNumber_freshVar_8= true ;}}}if ((tomMatch163NameNumber_freshVar_8 ==  false )) {if ( true ) {











        // if this is not an array, nothing to do
        if(!TomBase.isArrayOperator(Compiler.getSymbolTable().
            getSymbolFromName(tomMatch163NameNumber_freshVar_6))) { return tom_m; }
        Constraint detachedConstr = GeneralPurposePropagator.detachSublists(tom_m);
        // if something changed
        if (detachedConstr != tom_m) { return detachedConstr; }
      }}}}}}}}}}}}}}}}}}}if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {{  tom.engine.adt.tomconstraint.types.Constraint  tomMatch163NameNumberfreshSubject_1=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);if ( (tomMatch163NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch163NameNumber_freshVar_9= tomMatch163NameNumberfreshSubject_1.getpattern() ;{  tom.engine.adt.tomterm.types.TomTerm  tomMatch163NameNumber_freshVar_10= tomMatch163NameNumberfreshSubject_1.getsubject() ;if ( (tomMatch163NameNumber_freshVar_9 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{  tom.engine.adt.tomoption.types.OptionList  tomMatch163NameNumber_freshVar_11= tomMatch163NameNumber_freshVar_9.getOption() ;{  tom.engine.adt.tomname.types.TomNameList  tomMatch163NameNumber_freshVar_12= tomMatch163NameNumber_freshVar_9.getNameList() ;{  tom.engine.adt.tomslot.types.SlotList  tomMatch163NameNumber_freshVar_13= tomMatch163NameNumber_freshVar_9.getSlots() ;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch163NameNumber_freshVar_14= tomMatch163NameNumber_freshVar_9.getConstraints() ;if ( ((tomMatch163NameNumber_freshVar_12 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch163NameNumber_freshVar_12 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {{  tom.engine.adt.tomname.types.TomNameList  tomMatch163NameNumber_freshVar_15=tomMatch163NameNumber_freshVar_12;if (!( tomMatch163NameNumber_freshVar_15.isEmptyconcTomName() )) {if ( ( tomMatch163NameNumber_freshVar_15.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {{  String  tomMatch163NameNumber_freshVar_18=  tomMatch163NameNumber_freshVar_15.getHeadconcTomName() .getString() ;{  tom.engine.adt.tomname.types.TomName  tom_name= tomMatch163NameNumber_freshVar_15.getHeadconcTomName() ;{  tom.engine.adt.tomname.types.TomNameList  tomMatch163NameNumber_freshVar_16= tomMatch163NameNumber_freshVar_15.getTailconcTomName() ;{  tom.engine.adt.tomslot.types.SlotList  tom_slots=tomMatch163NameNumber_freshVar_13;{  tom.engine.adt.tomterm.types.TomTerm  tom_g=tomMatch163NameNumber_freshVar_10;{  tom.engine.adt.tomconstraint.types.Constraint  tom_m=tomMatch163NameNumberfreshSubject_1;{ boolean tomMatch163NameNumber_freshVar_20= false ;if ( (tomMatch163NameNumber_freshVar_10 instanceof tom.engine.adt.tomterm.types.tomterm.SymbolOf) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch163NameNumber_freshVar_19=tomMatch163NameNumber_freshVar_10;if ( tomMatch163NameNumber_freshVar_19.equals(tom_g) ) {tomMatch163NameNumber_freshVar_20= true ;}}}if ((tomMatch163NameNumber_freshVar_20 ==  false )) {if ( true ) {






      
            // if this is not an array, nothing to do
            if(!TomBase.isArrayOperator(Compiler.getSymbolTable().
                getSymbolFromName(tomMatch163NameNumber_freshVar_18))) {return tom_m;}        
            TomType termType = Compiler.getTermTypeFromTerm(tomMatch163NameNumber_freshVar_9);
            // declare fresh index = 0            
            TomTerm freshIndex = getFreshIndex();				
            Constraint freshIndexDeclaration =  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(freshIndex,  tom.engine.adt.tomterm.types.tomterm.TargetLanguageToTomTerm.make( tom.engine.adt.tomsignature.types.targetlanguage.ITL.make("0") ) ) ;
            Constraint l =  tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
    match:  {if ( (tom_slots instanceof tom.engine.adt.tomslot.types.SlotList) ) {{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumberfreshSubject_1=(( tom.engine.adt.tomslot.types.SlotList )tom_slots);if ( ((tomMatch164NameNumberfreshSubject_1 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch164NameNumberfreshSubject_1 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumber_freshVar_0=tomMatch164NameNumberfreshSubject_1;if ( tomMatch164NameNumber_freshVar_0.isEmptyconcSlot() ) {if ( true ) {

                l = tom_append_list_AndConstraint(l, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint.make(tom_name, tom_g, freshIndex) , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) );
              }}}}}}if ( (tom_slots instanceof tom.engine.adt.tomslot.types.SlotList) ) {{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumberfreshSubject_1=(( tom.engine.adt.tomslot.types.SlotList )tom_slots);if ( ((tomMatch164NameNumberfreshSubject_1 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch164NameNumberfreshSubject_1 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumber_freshVar_1=tomMatch164NameNumberfreshSubject_1;{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumber_begin_3=tomMatch164NameNumber_freshVar_1;{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumber_end_4=tomMatch164NameNumber_freshVar_1;do {{{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumber_freshVar_2=tomMatch164NameNumber_end_4;if (!( tomMatch164NameNumber_freshVar_2.isEmptyconcSlot() )) {if ( ( tomMatch164NameNumber_freshVar_2.getHeadconcSlot()  instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch164NameNumber_freshVar_7=  tomMatch164NameNumber_freshVar_2.getHeadconcSlot() .getAppl() ;{  tom.engine.adt.tomterm.types.TomTerm  tom_appl=tomMatch164NameNumber_freshVar_7;{  tom.engine.adt.tomslot.types.SlotList  tomMatch164NameNumber_freshVar_5= tomMatch164NameNumber_freshVar_2.getTailconcSlot() ;{  tom.engine.adt.tomslot.types.SlotList  tom_X=tomMatch164NameNumber_freshVar_5;if ( true ) {

                TomTerm newFreshIndex = getFreshIndex();                
          mAppl:{if ( (tom_appl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch165NameNumberfreshSubject_1=(( tom.engine.adt.tomterm.types.TomTerm )tom_appl);{ boolean tomMatch165NameNumber_freshVar_0= false ;if ( (tomMatch165NameNumberfreshSubject_1 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch165NameNumber_freshVar_0= true ;} else {if ( (tomMatch165NameNumberfreshSubject_1 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {tomMatch165NameNumber_freshVar_0= true ;}}if ((tomMatch165NameNumber_freshVar_0 ==  true )) {if ( true ) {


                    // if it is the last element               
// [pem] same remark: move the test outside the match
                    if(tom_X.length() == 0) {
                      // we should only assign it, without generating a loop
                      l = tom_append_list_AndConstraint(l, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_appl,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.GetSliceArray.make(tom_name, tom_g, freshIndex,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.GetSize.make(tom_name, tom_g) ) ) ) ) , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )
;
                    } else {
                      TomTerm beginIndex = getBeginIndex();
                      TomTerm endIndex = getEndIndex();
                      l = tom_append_list_AndConstraint(l, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(beginIndex, freshIndex) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(endIndex, freshIndex) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_appl,  tom.engine.adt.tomterm.types.tomterm.VariableHeadArray.make(tom_name, tom_g, beginIndex, endIndex) ) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(newFreshIndex, endIndex) , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ) ) )



;     
                    }
                    break mAppl;
                  }}}}}if ( (tom_appl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch165NameNumberfreshSubject_1=(( tom.engine.adt.tomterm.types.TomTerm )tom_appl);if ( true ) {
                    
                    l = tom_append_list_AndConstraint(l, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.Negate.make( tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint.make(tom_name, tom_g, freshIndex) ) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_appl,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.GetElement.make(tom_name, termType, tom_g, freshIndex) ) ) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(newFreshIndex,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.AddOne.make(freshIndex) ) ) , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ) )


;
                    // for the last element, we should also check that the list ends
                    if(tom_X.length() == 0) {                  
                      l = tom_append_list_AndConstraint(l, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint.make(tom_name, tom_g, newFreshIndex) , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) );
                    }
                  }}}}
// end match
                freshIndex = newFreshIndex;
              }}}}}}}}if ( tomMatch164NameNumber_end_4.isEmptyconcSlot() ) {tomMatch164NameNumber_end_4=tomMatch164NameNumber_begin_3;} else {tomMatch164NameNumber_end_4= tomMatch164NameNumber_end_4.getTailconcSlot() ;}}} while(!( tomMatch164NameNumber_end_4.equals(tomMatch164NameNumber_begin_3) ));}}}}}}}
// end match                        
            // add head equality condition + fresh var declaration + detached constraints
            l =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make( tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tomMatch163NameNumber_freshVar_11, tomMatch163NameNumber_freshVar_12,  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ,  tom.engine.adt.tomterm.types.tomterm.SymbolOf.make(tom_g) ) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(freshIndexDeclaration, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(ConstraintPropagator.performDetach(tom_m),tom_append_list_AndConstraint(l, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ) ) 
;
            return l;
        }}}}}}}}}}}}}}}}}}}}}}}}return super.visit_Constraint(tom__arg,introspector); }}private static  tom.library.sl.Strategy  tom_make_ArrayPatternMatching() { return new ArrayPatternMatching(); }

// end %strategy

  private static TomTerm getBeginIndex() {
    return Compiler.getBeginVariableStar(Compiler.getIntType());
  }

  private static TomTerm getEndIndex() {
    return Compiler.getEndVariableStar(Compiler.getIntType());
  }

  private static TomTerm getFreshIndex() {
    return Compiler.getFreshVariableStar(Compiler.getIntType());    
  }
}
