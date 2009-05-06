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

package tom.engine.tools;

import java.util.*;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.*;
import tom.engine.xml.Constants;

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

import tom.engine.exception.TomRuntimeException;
import aterm.ATerm;

public class ASTFactory {
        private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_append_list_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList l1,  tom.engine.adt.tomsignature.types.TomVisitList  l2) {     if( l1.isEmptyconcTomVisit() ) {       return l2;     } else if( l2.isEmptyconcTomVisit() ) {       return l1;     } else if(  l1.getTailconcTomVisit() .isEmptyconcTomVisit() ) {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,tom_append_list_concTomVisit( l1.getTailconcTomVisit() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_get_slice_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList  begin,  tom.engine.adt.tomsignature.types.TomVisitList  end, tom.engine.adt.tomsignature.types.TomVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomVisit()  ||  (end== tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( begin.getHeadconcTomVisit() ,( tom.engine.adt.tomsignature.types.TomVisitList )tom_get_slice_concTomVisit( begin.getTailconcTomVisit() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {     if( l1.isEmptyconcPairNameDecl() ) {       return l2;     } else if( l2.isEmptyconcPairNameDecl() ) {       return l1;     } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }    
   // Suppresses default constructor, ensuring non-instantiability.
  private ASTFactory() {}

  public static TomList makeList(Collection<TomTerm> c) {
    TomTerm array[] = c.toArray(new TomTerm[0]);
    TomList list =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
    for(int i=array.length-1; i>=0 ; i--) {
      TomTerm term = array[i];
      list =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(term,tom_append_list_concTomTerm(list, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
    }
    return list;
  }

  public static InstructionList makeInstructionList(Collection c) {
    Object array[] = c.toArray();
    InstructionList list =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
    for(int i=array.length-1; i>=0 ; i--) {
      Object elt = array[i];
      Instruction term;
      if(elt instanceof TargetLanguage) {
        term =  tom.engine.adt.tominstruction.types.instruction.TargetLanguageToInstruction.make((TargetLanguage)elt) ;
      } else if(elt instanceof TomTerm) {
        term =  tom.engine.adt.tominstruction.types.instruction.TomTermToInstruction.make((TomTerm)elt) ;
          //System.out.println("term   = " + term);
      } else if(elt instanceof Instruction) {
        term = (Instruction)elt;
      } else {
        /* XXX: is this an error ? if yes, it should not be that silent */
        System.out.println("elt   = " + elt);
        term = (Instruction) elt;
      }
      list =  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(term,tom_append_list_concInstruction(list, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) ;
    }
    return list;
  }

  public static OptionList makeOptionList(List argumentList) {
    OptionList list =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      Object elt = argumentList.get(i);
      Option term;
      if(elt instanceof TomName) {
        term =  tom.engine.adt.tomoption.types.option.TomNameToOption.make((TomName)elt) ;
      } else if(elt instanceof Declaration) {
        term =  tom.engine.adt.tomoption.types.option.DeclarationToOption.make((Declaration)elt) ;
      } else if(elt instanceof TomTerm) {
        term =  tom.engine.adt.tomoption.types.option.TomTermToOption.make((TomTerm)elt) ;
      } else {
        term = (Option) elt;
      }
      list =  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(term,tom_append_list_concOption(list, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ;
    }
    return list;
  }

  public static ConstraintList makeConstraintList(List<Constraint> argumentList) {
    ConstraintList list =  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make(argumentList.get(i),tom_append_list_concConstraint(list, tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() )) ;
    }
    return list;
  }

  public static ConstraintInstructionList makeConstraintInstructionList(List<ConstraintInstruction> argumentList) {
    ConstraintInstructionList list =  tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make(argumentList.get(i),tom_append_list_concConstraintInstruction(list, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
    }
    return list;
  }

  public static Constraint makeAndConstraint(List<Constraint> argumentList) {
    Constraint list =  tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(argumentList.get(i),tom_append_list_AndConstraint(list, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;
    }
    return list;
  }

  public static Constraint makeOrConstraint(List<Constraint> argumentList) {
    Constraint list =  tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(argumentList.get(i),tom_append_list_OrConstraint(list, tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )) ;
    }
    return list;
  }

  public static TomNameList makeNameList(List<TomName> argumentList) {
    TomNameList list =  tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(argumentList.get(i),tom_append_list_concTomName(list, tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() )) ;
    }
    return list;
  }

  public static SlotList makeSlotList(List<Slot> argumentList) {
    SlotList list =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(argumentList.get(i),tom_append_list_concSlot(list, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
    }
    return list;
  }

  public static PairNameDeclList makePairNameDeclList(List<PairNameDecl> argumentList) {
    PairNameDeclList list =  tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make(argumentList.get(i),tom_append_list_concPairNameDecl(list, tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() )) ;
    }
    return list;
  }

  public static TomVisitList makeTomVisitList(List<TomVisit> argumentList) {
    TomVisitList list =  tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() ;
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      list =  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make(argumentList.get(i),tom_append_list_concTomVisit(list, tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() )) ;
    }
    return list;
  }

  public static TomSymbol makeSymbol(String symbolName, TomType resultType, TomTypeList typeList,
      PairNameDeclList pairNameDeclList, List optionList) {
    return  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make( tom.engine.adt.tomname.types.tomname.Name.make(symbolName) ,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make(typeList, resultType) , pairNameDeclList, makeOptionList(optionList)) ;
  }

  public static OptionList makeOption(Option arg) {
    OptionList list =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
    if(arg!= null) {
      list =  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(arg,tom_append_list_concOption(list, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ;
    }
    return list;
  }

  public static ConstraintList makeConstraint(Constraint arg) {
    ConstraintList list =  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ;
    if(arg!= null) {
      list =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make(arg,tom_append_list_concConstraint(list, tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() )) ;
    }
    return list;
  }

  public static Constraint makeAssignTo(TomName name,int line, String fileName) {
    return  tom.engine.adt.tomconstraint.types.constraint.AssignTo.make( tom.engine.adt.tomterm.types.tomterm.Variable.make(makeOption(makeOriginTracking(name.getString(),line,fileName)), name, SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) 


;
  }

  public static Constraint makeStorePosition(TomName name,int line, String fileName) {
    return  tom.engine.adt.tomconstraint.types.constraint.AssignPositionTo.make( tom.engine.adt.tomterm.types.tomterm.Variable.make(makeOption(makeOriginTracking(name.getString(),line,fileName)), name, SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) 


;
  }

  public static OptionList makeOption(Option arg, Option info) {
    OptionList list =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
    if(arg!= null) {
      list =  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(arg,tom_append_list_concOption(list, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ;
    }
    list =  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(info,tom_append_list_concOption(list, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ;
    return list;
  }

  private static Option makeOriginTracking(String name, int line , String fileName) {
    return  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(name) , line, fileName) ;
  }

  protected static TomType makeType(String typeNameTom, String typeNametGL) {
    TomType typeTom =  tom.engine.adt.tomtype.types.tomtype.ASTTomType.make(typeNameTom) ;
    TomType sortTL  =  tom.engine.adt.tomtype.types.tomtype.TLType.make( tom.engine.adt.tomsignature.types.targetlanguage.ITL.make(typeNametGL) ) ;
    return  tom.engine.adt.tomtype.types.tomtype.Type.make(typeTom, sortTL) ;
  }

    /*
     * create an <sort> symbol
     * where <sort> could be int. double or String
     */
  private static void makeSortSymbol(SymbolTable symbolTable,
                             String sort,
                             String value, List optionList) {
    TomTypeList typeList =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
    PairNameDeclList pairSlotDeclList =  tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ;
    TomSymbol astSymbol = makeSymbol(value, tom.engine.adt.tomtype.types.tomtype.TomTypeAlone.make(sort) ,typeList,pairSlotDeclList,optionList);
    symbolTable.putSymbol(value,astSymbol);
  }

    /*
     * create an integer symbol
     */
  public static void makeIntegerSymbol(SymbolTable symbolTable,
                                String value, List optionList) {
    String sort = "int";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a long symbol
     */
  public static void makeLongSymbol(SymbolTable symbolTable,
                             String value, List optionList) {
    String sort = "long";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a char symbol
     */
  public static void makeCharSymbol(SymbolTable symbolTable,
                             String value, List optionList) {
    String sort = "char";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }
    /*
     * create a double symbol
     */
  public static void makeDoubleSymbol(SymbolTable symbolTable,
                               String value, List optionList) {
    String sort = "double";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a string symbol
     */
  public static void makeStringSymbol(SymbolTable symbolTable,
                               String value, List optionList) {
    String sort = "String";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * update the root of lhs: it becomes a defined symbol
     */
  public static TomSymbol updateDefinedSymbol(SymbolTable symbolTable, TomTerm term) {
    if(term.isTermAppl() || term.isRecordAppl()) {
      String key = term.getNameList().getHeadconcTomName().getString();
      TomSymbol symbol = symbolTable.getSymbolFromName(key);
      if(symbol != null) {
        OptionList optionList = symbol.getOption();
        optionList = tom_append_list_concOption(optionList, tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.DefinedSymbol.make() , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) );
        symbolTable.putSymbol(key,symbol.setOption(optionList));
        return symbol;
      }
    }
    return null;
  }

  public static String makeSingleLineCode(String code, boolean pretty) {
    if(!pretty) {
      code = code.replace('\n', ' ');
      code = code.replace('\t', ' ');
      code = code.replace('\r', ' ');
    }
    return code;
  }

  public static TomName makeName(String slotName) {
    if(slotName.length()>0) {
      return  tom.engine.adt.tomname.types.tomname.Name.make(slotName) ;
    } else {
      return  tom.engine.adt.tomname.types.tomname.EmptyName.make() ;
    }
  }

  public static String encodeXMLString(SymbolTable symbolTable, String name) {
    name = "\"" + name + "\"";
    makeStringSymbol(symbolTable,name, new LinkedList());
    return name;
  }

  public static String makeTomVariableName(String name) {
    return "tom_" + name;
  }

  public static TomList metaEncodeTermList(SymbolTable symbolTable,TomList list) {
    {{if ( (list instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )list) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )list) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if ( (( tom.engine.adt.tomterm.types.TomList )list).isEmptyconcTomTerm() ) {
 return  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;}}}}{if ( (list instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )list) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )list) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )list).isEmptyconcTomTerm() )) {

        TomList tl = metaEncodeTermList(symbolTable, (( tom.engine.adt.tomterm.types.TomList )list).getTailconcTomTerm() );
        return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(metaEncodeXMLAppl(symbolTable, (( tom.engine.adt.tomterm.types.TomList )list).getHeadconcTomTerm() ),tom_append_list_concTomTerm(tl, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
      }}}}}

    return list;
  }

  public static TomTerm encodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * encode a String into a quoted-string
       * Appl(...,Name("string"),...) becomes
       * Appl(...,Name("\"string\""),...)
       */
    TomNameList newNameList =  tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ;
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch218NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch218NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch218NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch218NameNumber_end_6=tomMatch218NameNumber_freshVar_1;do {{if (!( tomMatch218NameNumber_end_6.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch218NameNumber_freshVar_10= tomMatch218NameNumber_end_6.getHeadconcTomName() ;if ( (tomMatch218NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        newNameList = tom_append_list_concTomName(newNameList, tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(encodeXMLString(symbolTable, tomMatch218NameNumber_freshVar_10.getString() )) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) );
      }}if ( tomMatch218NameNumber_end_6.isEmptyconcTomName() ) {tomMatch218NameNumber_end_6=tomMatch218NameNumber_freshVar_1;} else {tomMatch218NameNumber_end_6= tomMatch218NameNumber_end_6.getTailconcTomName() ;}}} while(!( (tomMatch218NameNumber_end_6==tomMatch218NameNumber_freshVar_1) ));}}}}}

    term = term.setNameList(newNameList);
      //System.out.println("encodeXMLAppl = " + term);
    return term;
  }

  public static TomTerm metaEncodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * meta-encode a String into a TextNode
       * Appl(...,Name("\"string\""),...) becomes
       * Appl(...,Name("TextNode"),[Appl(...,Name("\"string\""),...)],...)
       */
      //System.out.println("metaEncode: " + term);
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch219NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch219NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch219NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch219NameNumber_freshVar_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch219NameNumber_freshVar_6= tomMatch219NameNumber_freshVar_1.getHeadconcTomName() ;if ( (tomMatch219NameNumber_freshVar_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch219NameNumber_freshVar_1.getTailconcTomName() .isEmptyconcTomName() ) {

          //System.out.println("tomName = " + tomName);
        TomSymbol tomSymbol = symbolTable.getSymbolFromName( tomMatch219NameNumber_freshVar_6.getString() );
        if(tomSymbol != null) {
          if(symbolTable.isStringType(TomBase.getTomType(TomBase.getSymbolCodomain(tomSymbol)))) {
            Option info =  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.TEXT_NODE) , -1, "unknown filename") ;
            term =  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(ASTFactory.makeOption(info),  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.TEXT_NODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ,  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_DATA) , term) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) 

;
              //System.out.println("metaEncodeXmlAppl = " + term);
          }
        }
      }}}}}}}}

    return term;
  }

  public static boolean isExplicitTermList(List childs) {
    if(childs.size() == 1) {
      TomTerm term = (TomTerm) childs.get(0);
      {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch220NameNumber_freshVar_8= false ; tom.engine.adt.tomname.types.TomNameList  tomMatch220NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch220NameNumber_freshVar_8= true ;tomMatch220NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch220NameNumber_freshVar_8= true ;tomMatch220NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;}}}if ((tomMatch220NameNumber_freshVar_8 ==  true )) {if ( ((tomMatch220NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch220NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch220NameNumber_freshVar_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch220NameNumber_freshVar_6= tomMatch220NameNumber_freshVar_1.getHeadconcTomName() ;if ( (tomMatch220NameNumber_freshVar_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "".equals( tomMatch220NameNumber_freshVar_6.getString() ) ) {if (  tomMatch220NameNumber_freshVar_1.getTailconcTomName() .isEmptyconcTomName() ) {

          return true;
        }}}}}}}}}

    }
    return false;
  }

  public static List<TomTerm> metaEncodeExplicitTermList(SymbolTable symbolTable, TomTerm term) {
    LinkedList<TomTerm> list = new LinkedList<TomTerm>();
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch221NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch221NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch221NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch221NameNumber_freshVar_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch221NameNumber_freshVar_7= tomMatch221NameNumber_freshVar_1.getHeadconcTomName() ;if ( (tomMatch221NameNumber_freshVar_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "".equals( tomMatch221NameNumber_freshVar_7.getString() ) ) {if (  tomMatch221NameNumber_freshVar_1.getTailconcTomName() .isEmptyconcTomName() ) { tom.engine.adt.tomslot.types.SlotList  tom_args= (( tom.engine.adt.tomterm.types.TomTerm )term).getSlots() ;

        while(!tom_args.isEmptyconcSlot()) {
          list.add(metaEncodeXMLAppl(symbolTable,tom_args.getHeadconcSlot().getAppl()));
          tom_args= tom_args.getTailconcSlot();
        }
        return list;
      }}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch221NameNumber_freshVar_10= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch221NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch221NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch221NameNumber_freshVar_10.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch221NameNumber_freshVar_16= tomMatch221NameNumber_freshVar_10.getHeadconcTomName() ;if ( (tomMatch221NameNumber_freshVar_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "".equals( tomMatch221NameNumber_freshVar_16.getString() ) ) {if (  tomMatch221NameNumber_freshVar_10.getTailconcTomName() .isEmptyconcTomName() ) { tom.engine.adt.tomterm.types.TomList  tom_args= (( tom.engine.adt.tomterm.types.TomTerm )term).getArgs() ;


        while(!tom_args.isEmptyconcTomTerm()) {
          list.add(metaEncodeXMLAppl(symbolTable,tom_args.getHeadconcTomTerm()));
          tom_args= tom_args.getTailconcTomTerm();
        }
        return list;
      }}}}}}}}}

    //System.out.println("metaEncodeExplicitTermList: strange case: " + term);
    list.add(term);
    return list;
  }

  public static TomTerm buildList(TomName name,TomList args, SymbolTable symbolTable) {
    TomSymbol topListSymbol = symbolTable.getSymbolFromName(name.getString());
    String topDomain = TomBase.getTomType(TomBase.getSymbolDomain(topListSymbol).getHeadconcTomType());
    String topCodomain = TomBase.getTomType(TomBase.getSymbolCodomain(topListSymbol));
    {{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if ( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() ) {

        return  tom.engine.adt.tomterm.types.tomterm.BuildEmptyList.make(name) ;
      }}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) {if ( ( (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm()  instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        /* a VariableStar is always flattened */
        return  tom.engine.adt.tomterm.types.tomterm.BuildAppendList.make(name,  (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() , subList) ;
      }}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_12= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_12 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) { tom.engine.adt.tomterm.types.TomList  tomMatch222NameNumber_freshVar_11= tomMatch222NameNumber_freshVar_12.getArgs() ;if ( ((tomMatch222NameNumber_freshVar_11 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch222NameNumber_freshVar_11 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch222NameNumber_freshVar_11.isEmptyconcTomTerm() )) {if ( ( tomMatch222NameNumber_freshVar_11.getHeadconcTomTerm()  instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        /* a VariableStar is always flattened */
        return  tom.engine.adt.tomterm.types.tomterm.BuildAppendList.make(name,  (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() , subList) ;
      }}}}}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_22= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_22 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomterm.types.TomTerm  tom_head= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;


        //System.out.println("topDomain = " + topDomain);
        //System.out.println("topCodomain = " + topCodomain);
        //System.out.println("varType = " + TomBase.getTomType(`varType));

        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        /* a Variable is flattened if type and codomain are equals */
        if(topDomain != topCodomain) {
          if(TomBase.getTomType( tomMatch222NameNumber_freshVar_22.getAstType() ) == topCodomain) {
            return  tom.engine.adt.tomterm.types.tomterm.BuildAppendList.make(name, tom_head, subList) ;
          }
        }
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsList.make(name, tom_head, subList) ;
      }}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_28= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_28 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) { tom.engine.adt.tomterm.types.TomList  tomMatch222NameNumber_freshVar_27= tomMatch222NameNumber_freshVar_28.getArgs() ;if ( ((tomMatch222NameNumber_freshVar_27 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch222NameNumber_freshVar_27 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch222NameNumber_freshVar_27.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_33= tomMatch222NameNumber_freshVar_27.getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_33 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomterm.types.TomTerm  tom_head= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;


        //System.out.println("topDomain = " + topDomain);
        //System.out.println("topCodomain = " + topCodomain);
        //System.out.println("varType = " + TomBase.getTomType(`varType));

        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        /* a Variable is flattened if type and codomain are equals */
        if(topDomain != topCodomain) {
          if(TomBase.getTomType( tomMatch222NameNumber_freshVar_33.getAstType() ) == topCodomain) {
            return  tom.engine.adt.tomterm.types.tomterm.BuildAppendList.make(name, tom_head, subList) ;
          }
        }
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsList.make(name, tom_head, subList) ;
      }}}}}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_39= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_39 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) { tom.engine.adt.tomterm.types.TomList  tomMatch222NameNumber_freshVar_38= tomMatch222NameNumber_freshVar_39.getArgs() ;if ( ((tomMatch222NameNumber_freshVar_38 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch222NameNumber_freshVar_38 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch222NameNumber_freshVar_38.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_44= tomMatch222NameNumber_freshVar_38.getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_44 instanceof tom.engine.adt.tomterm.types.tomterm.BuildConsList) ) { tom.engine.adt.tomterm.types.TomTerm  tom_head= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;


        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        /* Flatten nested lists, unless domain and codomain are equals */
        if(topDomain != topCodomain) {
          if(name.equals( tomMatch222NameNumber_freshVar_44.getAstName() )) {
            return  tom.engine.adt.tomterm.types.tomterm.BuildAppendList.make(name, tom_head, subList) ;
          }
        }
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsList.make(name, tom_head, subList) ;
      }}}}}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_50= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_50 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) { tom.engine.adt.tomterm.types.TomList  tomMatch222NameNumber_freshVar_49= tomMatch222NameNumber_freshVar_50.getArgs() ;if ( ((tomMatch222NameNumber_freshVar_49 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch222NameNumber_freshVar_49 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch222NameNumber_freshVar_49.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_55= tomMatch222NameNumber_freshVar_49.getHeadconcTomTerm() ;if ( (tomMatch222NameNumber_freshVar_55 instanceof tom.engine.adt.tomterm.types.tomterm.BuildTerm) ) { tom.engine.adt.tomname.types.TomName  tomMatch222NameNumber_freshVar_54= tomMatch222NameNumber_freshVar_55.getAstName() ;if ( (tomMatch222NameNumber_freshVar_54 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomterm.types.TomTerm  tom_head= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;


        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        if(topDomain != topCodomain) {
        /*
         * compare the codomain of tomName with topDomain
         * if the codomain of the inserted element is equal to the codomain
         * of the list operator, a BuildAppendList is performed
         */
          TomSymbol symbol = symbolTable.getSymbolFromName( tomMatch222NameNumber_freshVar_54.getString() );
          String codomain = TomBase.getTomType(TomBase.getSymbolCodomain(symbol));
          if(codomain == topCodomain) {
            return  tom.engine.adt.tomterm.types.tomterm.BuildAppendList.make(name, tom_head, subList) ;
          }
        }
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsList.make(name, tom_head, subList) ;
      }}}}}}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch222NameNumber_freshVar_62= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;boolean tomMatch222NameNumber_freshVar_63= false ;if ( (tomMatch222NameNumber_freshVar_62 instanceof tom.engine.adt.tomterm.types.tomterm.BuildTerm) ) {tomMatch222NameNumber_freshVar_63= true ;} else {if ( (tomMatch222NameNumber_freshVar_62 instanceof tom.engine.adt.tomterm.types.tomterm.BuildConstant) ) {tomMatch222NameNumber_freshVar_63= true ;} else {if ( (tomMatch222NameNumber_freshVar_62 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) {tomMatch222NameNumber_freshVar_63= true ;} else {if ( (tomMatch222NameNumber_freshVar_62 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch222NameNumber_freshVar_63= true ;} else {if ( (tomMatch222NameNumber_freshVar_62 instanceof tom.engine.adt.tomterm.types.tomterm.BuildAppendList) ) {tomMatch222NameNumber_freshVar_63= true ;} else {if ( (tomMatch222NameNumber_freshVar_62 instanceof tom.engine.adt.tomterm.types.tomterm.BuildConsList) ) {tomMatch222NameNumber_freshVar_63= true ;}}}}}}if ((tomMatch222NameNumber_freshVar_63 ==  true )) {


        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsList.make(name,  (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() , subList) ;
      }}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) {if ( ( (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm()  instanceof tom.engine.adt.tomterm.types.tomterm.TargetLanguageToTomTerm) ) {


        TomTerm subList = buildList(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,symbolTable);
        return subList;
      }}}}}}


    throw new TomRuntimeException("buildList strange term: " + args);
  }

  public static TomTerm buildArray(TomName name,TomList args, SymbolTable symbolTable) {
    return buildArray(name,args.reverse(),0, symbolTable);
  }

  private static TomTerm buildArray(TomName name,TomList args, int size, SymbolTable symbolTable) {
    TomSymbol topListSymbol = symbolTable.getSymbolFromName(name.getString());
    String topDomain = TomBase.getTomType(TomBase.getSymbolDomain(topListSymbol).getHeadconcTomType());
    String topCodomain = TomBase.getTomType(TomBase.getSymbolCodomain(topListSymbol));

    {{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if ( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() ) {

        return  tom.engine.adt.tomterm.types.tomterm.BuildEmptyArray.make(name, size) ;
      }}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) {if ( ( (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm()  instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        TomTerm subList = buildArray(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,size+1,symbolTable);
        /* a VariableStar is always flattened */
        return  tom.engine.adt.tomterm.types.tomterm.BuildAppendArray.make(name,  (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() , subList) ;
      }}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch223NameNumber_freshVar_12= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch223NameNumber_freshVar_12 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) { tom.engine.adt.tomterm.types.TomList  tomMatch223NameNumber_freshVar_11= tomMatch223NameNumber_freshVar_12.getArgs() ;if ( ((tomMatch223NameNumber_freshVar_11 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch223NameNumber_freshVar_11 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch223NameNumber_freshVar_11.isEmptyconcTomTerm() )) {if ( ( tomMatch223NameNumber_freshVar_11.getHeadconcTomTerm()  instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        TomTerm subList = buildArray(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,size+1,symbolTable);
        /* a VariableStar is always flattened */
        return  tom.engine.adt.tomterm.types.tomterm.BuildAppendArray.make(name,  (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() , subList) ;
      }}}}}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch223NameNumber_freshVar_22= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch223NameNumber_freshVar_22 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) { tom.engine.adt.tomterm.types.TomList  tomMatch223NameNumber_freshVar_21= tomMatch223NameNumber_freshVar_22.getArgs() ;if ( ((tomMatch223NameNumber_freshVar_21 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch223NameNumber_freshVar_21 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch223NameNumber_freshVar_21.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch223NameNumber_freshVar_27= tomMatch223NameNumber_freshVar_21.getHeadconcTomTerm() ;if ( (tomMatch223NameNumber_freshVar_27 instanceof tom.engine.adt.tomterm.types.tomterm.BuildConsArray) ) { tom.engine.adt.tomterm.types.TomTerm  tom_head= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;


        TomTerm subList = buildArray(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,size+1,symbolTable);
        /* Flatten nested lists, unless domain and codomain are equals */
        if(topDomain != topCodomain) {
          if(name.equals( tomMatch223NameNumber_freshVar_27.getAstName() )) {
            return  tom.engine.adt.tomterm.types.tomterm.BuildAppendArray.make(name, tom_head, subList) ;
          }
        }
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsArray.make(name, tom_head, subList) ;
      }}}}}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch223NameNumber_freshVar_33= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;if ( (tomMatch223NameNumber_freshVar_33 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) { tom.engine.adt.tomterm.types.TomList  tomMatch223NameNumber_freshVar_32= tomMatch223NameNumber_freshVar_33.getArgs() ;if ( ((tomMatch223NameNumber_freshVar_32 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch223NameNumber_freshVar_32 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch223NameNumber_freshVar_32.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch223NameNumber_freshVar_38= tomMatch223NameNumber_freshVar_32.getHeadconcTomTerm() ;if ( (tomMatch223NameNumber_freshVar_38 instanceof tom.engine.adt.tomterm.types.tomterm.BuildTerm) ) { tom.engine.adt.tomname.types.TomName  tomMatch223NameNumber_freshVar_37= tomMatch223NameNumber_freshVar_38.getAstName() ;if ( (tomMatch223NameNumber_freshVar_37 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomterm.types.TomTerm  tom_head= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;


        TomTerm subList = buildArray(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,size+1,symbolTable);
        if(topDomain != topCodomain) {
        /*
         * compare the codomain of tomName with topDomain
         * if the codomain of the inserted element is equal to the codomain
         * of the list operator, a BuildAppendArray is performed
         */
          TomSymbol symbol = symbolTable.getSymbolFromName( tomMatch223NameNumber_freshVar_37.getString() );
          String codomain = TomBase.getTomType(TomBase.getSymbolCodomain(symbol));
          if(codomain == topCodomain) {
            return  tom.engine.adt.tomterm.types.tomterm.BuildAppendArray.make(name, tom_head, subList) ;
          }
        }
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsArray.make(name, tom_head, subList) ;
      }}}}}}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch223NameNumber_freshVar_45= (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() ;boolean tomMatch223NameNumber_freshVar_46= false ;if ( (tomMatch223NameNumber_freshVar_45 instanceof tom.engine.adt.tomterm.types.tomterm.BuildTerm) ) {tomMatch223NameNumber_freshVar_46= true ;} else {if ( (tomMatch223NameNumber_freshVar_45 instanceof tom.engine.adt.tomterm.types.tomterm.BuildConstant) ) {tomMatch223NameNumber_freshVar_46= true ;} else {if ( (tomMatch223NameNumber_freshVar_45 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch223NameNumber_freshVar_46= true ;} else {if ( (tomMatch223NameNumber_freshVar_45 instanceof tom.engine.adt.tomterm.types.tomterm.Composite) ) {tomMatch223NameNumber_freshVar_46= true ;}}}}if ((tomMatch223NameNumber_freshVar_46 ==  true )) {

        TomTerm subList = buildArray(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,size+1,symbolTable);
        return  tom.engine.adt.tomterm.types.tomterm.BuildConsArray.make(name,  (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm() , subList) ;
      }}}}}{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )args).isEmptyconcTomTerm() )) {if ( ( (( tom.engine.adt.tomterm.types.TomList )args).getHeadconcTomTerm()  instanceof tom.engine.adt.tomterm.types.tomterm.TargetLanguageToTomTerm) ) {


        TomTerm subList = buildArray(name, (( tom.engine.adt.tomterm.types.TomList )args).getTailconcTomTerm() ,size,symbolTable);
        return subList;
      }}}}}}



    throw new TomRuntimeException("buildArray strange term: " + args);
  }

  /*
   * transform a string "...$t...$u..." into "...{0}...{1}..."
   */
  public static String abstractCode(String code, String... vars) {
    int index=0;
    for(String var:vars) {
      code = code.replace("$"+var,"{"+index+"}");
      index++;
    }
    return code;
  }

}
