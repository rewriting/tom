/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2017, Universite de Lorraine
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

package tom.engine.parser.antlr4;

import java.util.logging.Logger;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

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
import tom.engine.adt.theory.types.*;
import tom.engine.adt.cst.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;
import tom.engine.parser.TomParserTool;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

//import tom.library.sl.*;

public class AstBuilder {
  // %include { ../../../library/mapping/java/sl.tom}
     private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_append_list_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList l1,  tom.engine.adt.tomsignature.types.TomVisitList  l2) {     if( l1.isEmptyconcTomVisit() ) {       return l2;     } else if( l2.isEmptyconcTomVisit() ) {       return l1;     } else if(  l1.getTailconcTomVisit() .isEmptyconcTomVisit() ) {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,tom_append_list_concTomVisit( l1.getTailconcTomVisit() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_get_slice_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList  begin,  tom.engine.adt.tomsignature.types.TomVisitList  end, tom.engine.adt.tomsignature.types.TomVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomVisit()  ||  (end== tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( begin.getHeadconcTomVisit() ,( tom.engine.adt.tomsignature.types.TomVisitList )tom_get_slice_concTomVisit( begin.getTailconcTomVisit() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_append_list_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList l1,  tom.engine.adt.tomdeclaration.types.DeclarationList  l2) {     if( l1.isEmptyconcDeclaration() ) {       return l2;     } else if( l2.isEmptyconcDeclaration() ) {       return l1;     } else if(  l1.getTailconcDeclaration() .isEmptyconcDeclaration() ) {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,l2) ;     } else {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,tom_append_list_concDeclaration( l1.getTailconcDeclaration() ,l2)) ;     }   }   private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_get_slice_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList  begin,  tom.engine.adt.tomdeclaration.types.DeclarationList  end, tom.engine.adt.tomdeclaration.types.DeclarationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcDeclaration()  ||  (end== tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( begin.getHeadconcDeclaration() ,( tom.engine.adt.tomdeclaration.types.DeclarationList )tom_get_slice_concDeclaration( begin.getTailconcDeclaration() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {     if( l1.isEmptyconcTypeOption() ) {       return l2;     } else if( l2.isEmptyconcTypeOption() ) {       return l1;     } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {     if( l1.isEmptyconcCode() ) {       return l2;     } else if( l2.isEmptyconcCode() ) {       return l1;     } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;     } else {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_AndConstraint() ) {       return l2;     } else if( l2.isEmptyCst_AndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {       if(  l1.getTailCst_AndConstraint() .isEmptyCst_AndConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,tom_append_list_Cst_AndConstraint( l1.getTailCst_AndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_AndConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getHeadCst_AndConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_AndConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getTailCst_AndConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_OrConstraint() ) {       return l2;     } else if( l2.isEmptyCst_OrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {       if(  l1.getTailCst_OrConstraint() .isEmptyCst_OrConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,tom_append_list_Cst_OrConstraint( l1.getTailCst_OrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_OrConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getHeadCst_OrConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_OrConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getTailCst_OrConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSlotList  tom_append_list_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList l1,  tom.engine.adt.cst.types.CstSlotList  l2) {     if( l1.isEmptyConcCstSlot() ) {       return l2;     } else if( l2.isEmptyConcCstSlot() ) {       return l1;     } else if(  l1.getTailConcCstSlot() .isEmptyConcCstSlot() ) {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,tom_append_list_ConcCstSlot( l1.getTailConcCstSlot() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSlotList  tom_get_slice_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList  begin,  tom.engine.adt.cst.types.CstSlotList  end, tom.engine.adt.cst.types.CstSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSlot()  ||  (end== tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( begin.getHeadConcCstSlot() ,( tom.engine.adt.cst.types.CstSlotList )tom_get_slice_ConcCstSlot( begin.getTailConcCstSlot() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBlockList  tom_append_list_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList l1,  tom.engine.adt.cst.types.CstBlockList  l2) {     if( l1.isEmptyConcCstBlock() ) {       return l2;     } else if( l2.isEmptyConcCstBlock() ) {       return l1;     } else if(  l1.getTailConcCstBlock() .isEmptyConcCstBlock() ) {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,tom_append_list_ConcCstBlock( l1.getTailConcCstBlock() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBlockList  tom_get_slice_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList  begin,  tom.engine.adt.cst.types.CstBlockList  end, tom.engine.adt.cst.types.CstBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBlock()  ||  (end== tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( begin.getHeadConcCstBlock() ,( tom.engine.adt.cst.types.CstBlockList )tom_get_slice_ConcCstBlock( begin.getTailConcCstBlock() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_append_list_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList l1,  tom.engine.adt.cst.types.CstPairSlotBQTermList  l2) {     if( l1.isEmptyConcCstPairSlotBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstPairSlotBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstPairSlotBQTerm() .isEmptyConcCstPairSlotBQTerm() ) {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,tom_append_list_ConcCstPairSlotBQTerm( l1.getTailConcCstPairSlotBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_get_slice_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList  begin,  tom.engine.adt.cst.types.CstPairSlotBQTermList  end, tom.engine.adt.cst.types.CstPairSlotBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairSlotBQTerm()  ||  (end== tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( begin.getHeadConcCstPairSlotBQTerm() ,( tom.engine.adt.cst.types.CstPairSlotBQTermList )tom_get_slice_ConcCstPairSlotBQTerm( begin.getTailConcCstPairSlotBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOptionList  tom_append_list_ConcCstOption( tom.engine.adt.cst.types.CstOptionList l1,  tom.engine.adt.cst.types.CstOptionList  l2) {     if( l1.isEmptyConcCstOption() ) {       return l2;     } else if( l2.isEmptyConcCstOption() ) {       return l1;     } else if(  l1.getTailConcCstOption() .isEmptyConcCstOption() ) {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,tom_append_list_ConcCstOption( l1.getTailConcCstOption() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOptionList  tom_get_slice_ConcCstOption( tom.engine.adt.cst.types.CstOptionList  begin,  tom.engine.adt.cst.types.CstOptionList  end, tom.engine.adt.cst.types.CstOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOption()  ||  (end== tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( begin.getHeadConcCstOption() ,( tom.engine.adt.cst.types.CstOptionList )tom_get_slice_ConcCstOption( begin.getTailConcCstOption() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPatternList  tom_append_list_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList l1,  tom.engine.adt.cst.types.CstPatternList  l2) {     if( l1.isEmptyConcCstPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPattern() ) {       return l1;     } else if(  l1.getTailConcCstPattern() .isEmptyConcCstPattern() ) {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,tom_append_list_ConcCstPattern( l1.getTailConcCstPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPatternList  tom_get_slice_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList  begin,  tom.engine.adt.cst.types.CstPatternList  end, tom.engine.adt.cst.types.CstPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPattern()  ||  (end== tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( begin.getHeadConcCstPattern() ,( tom.engine.adt.cst.types.CstPatternList )tom_get_slice_ConcCstPattern( begin.getTailConcCstPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstNameList  tom_append_list_ConcCstName( tom.engine.adt.cst.types.CstNameList l1,  tom.engine.adt.cst.types.CstNameList  l2) {     if( l1.isEmptyConcCstName() ) {       return l2;     } else if( l2.isEmptyConcCstName() ) {       return l1;     } else if(  l1.getTailConcCstName() .isEmptyConcCstName() ) {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,tom_append_list_ConcCstName( l1.getTailConcCstName() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstNameList  tom_get_slice_ConcCstName( tom.engine.adt.cst.types.CstNameList  begin,  tom.engine.adt.cst.types.CstNameList  end, tom.engine.adt.cst.types.CstNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstName()  ||  (end== tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( begin.getHeadConcCstName() ,( tom.engine.adt.cst.types.CstNameList )tom_get_slice_ConcCstName( begin.getTailConcCstName() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOperatorList  tom_append_list_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList l1,  tom.engine.adt.cst.types.CstOperatorList  l2) {     if( l1.isEmptyConcCstOperator() ) {       return l2;     } else if( l2.isEmptyConcCstOperator() ) {       return l1;     } else if(  l1.getTailConcCstOperator() .isEmptyConcCstOperator() ) {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,tom_append_list_ConcCstOperator( l1.getTailConcCstOperator() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOperatorList  tom_get_slice_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList  begin,  tom.engine.adt.cst.types.CstOperatorList  end, tom.engine.adt.cst.types.CstOperatorList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOperator()  ||  (end== tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( begin.getHeadConcCstOperator() ,( tom.engine.adt.cst.types.CstOperatorList )tom_get_slice_ConcCstOperator( begin.getTailConcCstOperator() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstTermList  tom_append_list_ConcCstTerm( tom.engine.adt.cst.types.CstTermList l1,  tom.engine.adt.cst.types.CstTermList  l2) {     if( l1.isEmptyConcCstTerm() ) {       return l2;     } else if( l2.isEmptyConcCstTerm() ) {       return l1;     } else if(  l1.getTailConcCstTerm() .isEmptyConcCstTerm() ) {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,tom_append_list_ConcCstTerm( l1.getTailConcCstTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstTermList  tom_get_slice_ConcCstTerm( tom.engine.adt.cst.types.CstTermList  begin,  tom.engine.adt.cst.types.CstTermList  end, tom.engine.adt.cst.types.CstTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstTerm()  ||  (end== tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( begin.getHeadConcCstTerm() ,( tom.engine.adt.cst.types.CstTermList )tom_get_slice_ConcCstTerm( begin.getTailConcCstTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBQTermList  tom_append_list_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList l1,  tom.engine.adt.cst.types.CstBQTermList  l2) {     if( l1.isEmptyConcCstBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstBQTerm() .isEmptyConcCstBQTerm() ) {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,tom_append_list_ConcCstBQTerm( l1.getTailConcCstBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBQTermList  tom_get_slice_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList  begin,  tom.engine.adt.cst.types.CstBQTermList  end, tom.engine.adt.cst.types.CstBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBQTerm()  ||  (end== tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( begin.getHeadConcCstBQTerm() ,( tom.engine.adt.cst.types.CstBQTermList )tom_get_slice_ConcCstBQTerm( begin.getTailConcCstBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_append_list_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList l1,  tom.engine.adt.cst.types.CstConstraintActionList  l2) {     if( l1.isEmptyConcCstConstraintAction() ) {       return l2;     } else if( l2.isEmptyConcCstConstraintAction() ) {       return l1;     } else if(  l1.getTailConcCstConstraintAction() .isEmptyConcCstConstraintAction() ) {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,tom_append_list_ConcCstConstraintAction( l1.getTailConcCstConstraintAction() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_get_slice_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList  begin,  tom.engine.adt.cst.types.CstConstraintActionList  end, tom.engine.adt.cst.types.CstConstraintActionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstConstraintAction()  ||  (end== tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( begin.getHeadConcCstConstraintAction() ,( tom.engine.adt.cst.types.CstConstraintActionList )tom_get_slice_ConcCstConstraintAction( begin.getTailConcCstConstraintAction() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstVisitList  tom_append_list_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList l1,  tom.engine.adt.cst.types.CstVisitList  l2) {     if( l1.isEmptyConcCstVisit() ) {       return l2;     } else if( l2.isEmptyConcCstVisit() ) {       return l1;     } else if(  l1.getTailConcCstVisit() .isEmptyConcCstVisit() ) {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,tom_append_list_ConcCstVisit( l1.getTailConcCstVisit() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstVisitList  tom_get_slice_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList  begin,  tom.engine.adt.cst.types.CstVisitList  end, tom.engine.adt.cst.types.CstVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstVisit()  ||  (end== tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( begin.getHeadConcCstVisit() ,( tom.engine.adt.cst.types.CstVisitList )tom_get_slice_ConcCstVisit( begin.getTailConcCstVisit() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairPatternList  tom_append_list_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList l1,  tom.engine.adt.cst.types.CstPairPatternList  l2) {     if( l1.isEmptyConcCstPairPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPairPattern() ) {       return l1;     } else if(  l1.getTailConcCstPairPattern() .isEmptyConcCstPairPattern() ) {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,tom_append_list_ConcCstPairPattern( l1.getTailConcCstPairPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairPatternList  tom_get_slice_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList  begin,  tom.engine.adt.cst.types.CstPairPatternList  end, tom.engine.adt.cst.types.CstPairPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairPattern()  ||  (end== tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( begin.getHeadConcCstPairPattern() ,( tom.engine.adt.cst.types.CstPairPatternList )tom_get_slice_ConcCstPairPattern( begin.getTailConcCstPairPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSymbolList  tom_append_list_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList l1,  tom.engine.adt.cst.types.CstSymbolList  l2) {     if( l1.isEmptyConcCstSymbol() ) {       return l2;     } else if( l2.isEmptyConcCstSymbol() ) {       return l1;     } else if(  l1.getTailConcCstSymbol() .isEmptyConcCstSymbol() ) {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,tom_append_list_ConcCstSymbol( l1.getTailConcCstSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSymbolList  tom_get_slice_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList  begin,  tom.engine.adt.cst.types.CstSymbolList  end, tom.engine.adt.cst.types.CstSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSymbol()  ||  (end== tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( begin.getHeadConcCstSymbol() ,( tom.engine.adt.cst.types.CstSymbolList )tom_get_slice_ConcCstSymbol( begin.getTailConcCstSymbol() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.BQSlotList  tom_append_list_concBQSlot( tom.engine.adt.tomslot.types.BQSlotList l1,  tom.engine.adt.tomslot.types.BQSlotList  l2) {     if( l1.isEmptyconcBQSlot() ) {       return l2;     } else if( l2.isEmptyconcBQSlot() ) {       return l1;     } else if(  l1.getTailconcBQSlot() .isEmptyconcBQSlot() ) {       return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make( l1.getHeadconcBQSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make( l1.getHeadconcBQSlot() ,tom_append_list_concBQSlot( l1.getTailconcBQSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.BQSlotList  tom_get_slice_concBQSlot( tom.engine.adt.tomslot.types.BQSlotList  begin,  tom.engine.adt.tomslot.types.BQSlotList  end, tom.engine.adt.tomslot.types.BQSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQSlot()  ||  (end== tom.engine.adt.tomslot.types.bqslotlist.EmptyconcBQSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make( begin.getHeadconcBQSlot() ,( tom.engine.adt.tomslot.types.BQSlotList )tom_get_slice_concBQSlot( begin.getTailconcBQSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {     if( l1.isEmptyconcPairNameDecl() ) {       return l2;     } else if( l2.isEmptyconcPairNameDecl() ) {       return l1;     } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.theory.types.Theory  tom_append_list_concElementaryTheory( tom.engine.adt.theory.types.Theory l1,  tom.engine.adt.theory.types.Theory  l2) {     if( l1.isEmptyconcElementaryTheory() ) {       return l2;     } else if( l2.isEmptyconcElementaryTheory() ) {       return l1;     } else if(  l1.getTailconcElementaryTheory() .isEmptyconcElementaryTheory() ) {       return  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( l1.getHeadconcElementaryTheory() ,l2) ;     } else {       return  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( l1.getHeadconcElementaryTheory() ,tom_append_list_concElementaryTheory( l1.getTailconcElementaryTheory() ,l2)) ;     }   }   private static   tom.engine.adt.theory.types.Theory  tom_get_slice_concElementaryTheory( tom.engine.adt.theory.types.Theory  begin,  tom.engine.adt.theory.types.Theory  end, tom.engine.adt.theory.types.Theory  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcElementaryTheory()  ||  (end== tom.engine.adt.theory.types.theory.EmptyconcElementaryTheory.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( begin.getHeadconcElementaryTheory() ,( tom.engine.adt.theory.types.Theory )tom_get_slice_concElementaryTheory( begin.getTailconcElementaryTheory() ,end,tail)) ;   }   


  private static Logger logger = Logger.getLogger("tom.engine.typer.AstBuilder");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private SymbolTable symbolTable;
  private TomParserTool parserTool;

  public AstBuilder(TomParserTool parserTool,SymbolTable st) {
    this.parserTool = parserTool;
    this.symbolTable = st;
  }
  
  public TomParserTool getParserTool() {
    return this.parserTool;
  }

  public Code convert(CstProgram cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstProgram) ) {if ( ((( tom.engine.adt.cst.types.CstProgram )cst) instanceof tom.engine.adt.cst.types.cstprogram.Cst_Program) ) {

 
        return  tom.engine.adt.code.types.code.Tom.make(convertToCodeList( (( tom.engine.adt.cst.types.CstProgram )cst).getblocks() )) ;
      }}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Instruction convert(CstBlock cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {


        CstOption ot = getOriginTracking( (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() );
        String newline = System.getProperty("line.separator");
        /*
         * We suppose that TL is the last construct before a Tom construct
         * since content does not contain the ending newline, Tom code will be 
         * generated just after. This is a problem when content is a single
         * line comment. This is why we add a newline after each TL
         */
        /*
        return `CodeToInstruction(TargetLanguageToCode(TL(content + newline,
              TextPosition(ot.getstartLine(),ot.getstartColumn()),
              TextPosition(ot.getendLine()+1,0)))); // add a newline after TL
              */
         //* code without extra newline
        return  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.TL.make( (( tom.engine.adt.cst.types.CstBlock )cst).getcontent() ,  tom.engine.adt.tomsignature.types.textposition.TextPosition.make(ot.getstartLine(), ot.getstartColumn()) ,  tom.engine.adt.tomsignature.types.textposition.TextPosition.make(ot.getendLine(), ot.getendColumn()) ) ) ) 

;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_Metaquote) ) {



        return  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getblocks() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock) ) {



        return  tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction.make(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getbqterm() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_ReturnBQTerm) ) {



        return  tom.engine.adt.tominstruction.types.instruction.Return.make(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getbqterm() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_MatchConstruct) ) {



        ConstraintInstructionList cil = convert( (( tom.engine.adt.cst.types.CstBlock )cst).getconstraintActionList() , (( tom.engine.adt.cst.types.CstBlock )cst).getsubjectList() );
        return  tom.engine.adt.tominstruction.types.instruction.Match.make(cil, addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() ,"Match"))) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_StrategyConstruct) ) { tom.engine.adt.cst.types.CstName  tomMatch285_26= (( tom.engine.adt.cst.types.CstBlock )cst).getstratName() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch285_26) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch285_26.getname() ;



        TomName strategyName =  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) ;
        CstOption ot = getOriginTracking( (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() );
        BQTerm extendsBQTerm = convert( (( tom.engine.adt.cst.types.CstBlock )cst).getextendsTerm() );
        TomVisitList astVisitList = convert( (( tom.engine.adt.cst.types.CstBlock )cst).getvisitList() );
        Declaration strategyDecl =  tom.engine.adt.tomdeclaration.types.declaration.Strategy.make(strategyName, extendsBQTerm, astVisitList,  tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() , convert(ot,tom___name)) ;
        return  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.DeclarationToCode.make(strategyDecl) ) ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_TypetermConstruct) ) { tom.engine.adt.cst.types.CstType  tomMatch285_37= (( tom.engine.adt.cst.types.CstBlock )cst).gettypeName() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch285_37) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom___typeName= tomMatch285_37.gettype() ; tom.engine.adt.cst.types.CstType  tom___extendsTypeName= (( tom.engine.adt.cst.types.CstBlock )cst).getextendsTypeName() ; tom.engine.adt.cst.types.CstOperatorList  tom___operatorList= (( tom.engine.adt.cst.types.CstBlock )cst).getoperatorList() ;



        TypeOptionList typeoptionList =  tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ;
        DeclarationList declarationList =  tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ;
        { /* unamed block */{ /* unamed block */if ( (tom___extendsTypeName instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tom___extendsTypeName) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {

 typeoptionList =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.SubtypeDecl.make( (( tom.engine.adt.cst.types.CstType )tom___extendsTypeName).gettype() ) , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) ; }}}}{ /* unamed block */{ /* unamed block */if ( (tom___operatorList instanceof tom.engine.adt.cst.types.CstOperatorList) ) {if ( (((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator) || ((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator)) ) { tom.engine.adt.cst.types.CstOperatorList  tomMatch287_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);do {{ /* unamed block */if (!( tomMatch287_end_4.isEmptyConcCstOperator() )) { tom.engine.adt.cst.types.CstOperator  tom___operator= tomMatch287_end_4.getHeadConcCstOperator() ;{ /* unamed block */{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_Equals) ) { tom.engine.adt.cst.types.CstName  tomMatch288_1= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName1() ; tom.engine.adt.cst.types.CstName  tomMatch288_2= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName2() ; tom.engine.adt.cst.types.CstBlockList  tomMatch288_3= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch288_1) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name1= tomMatch288_1.getname() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch288_2) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name2= tomMatch288_2.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch288_3) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch288_3) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch288_3.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch288_16= tomMatch288_3.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch288_16) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch288_16.getoptionList() ;if (  tomMatch288_3.getTailConcCstBlock() .isEmptyConcCstBlock() ) {





                String code = ASTFactory.abstractCode( tomMatch288_16.getcontent() ,tom___name1,tom___name2);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl.make(makeBQVariableFromName(tom___name1,tom___typeName,tom___optionList2), makeBQVariableFromName(tom___name2,tom___typeName,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___typeName,tom___optionList2)) 




;
                declarationList =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(attribute,tom_append_list_concDeclaration(declarationList, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ; 
              }}}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_IsSort) ) { tom.engine.adt.cst.types.CstName  tomMatch288_19= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch288_20= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch288_19) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch288_19.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch288_20) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch288_20) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch288_20.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch288_30= tomMatch288_20.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch288_30) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch288_30.getoptionList() ;if (  tomMatch288_20.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch288_30.getcontent() ,tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl.make(makeBQVariableFromName(tom___name,tom___typeName,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___typeName,tom___optionList2)) 



;
                declarationList =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(attribute,tom_append_list_concDeclaration(declarationList, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ; 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_Implement) ) { tom.engine.adt.cst.types.CstBlockList  tomMatch288_33= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch288_33) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch288_33) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch288_33.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch288_40= tomMatch288_33.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch288_40) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {if (  tomMatch288_33.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                TomType astType =  tom.engine.adt.tomtype.types.tomtype.Type.make(typeoptionList, tom___typeName,  tom.engine.adt.tomtype.types.targetlanguagetype.TLType.make( tomMatch288_40.getcontent() ) ) ;
                symbolTable.putType(tom___typeName, astType);
              }}}}}}}}}if ( tomMatch287_end_4.isEmptyConcCstOperator() ) {tomMatch287_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);} else {tomMatch287_end_4= tomMatch287_end_4.getTailConcCstOperator() ;}}} while(!( (tomMatch287_end_4==(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList)) ));}}}}






        return  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.DeclarationToCode.make( tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___typeName) , declarationList, makeOriginTracking(tom___typeName, (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() )) ) ) 



;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_OpConstruct) ) { tom.engine.adt.cst.types.CstType  tomMatch285_47= (( tom.engine.adt.cst.types.CstBlock )cst).getcodomain() ; tom.engine.adt.cst.types.CstName  tomMatch285_48= (( tom.engine.adt.cst.types.CstBlock )cst).getctorName() ; tom.engine.adt.cst.types.CstOptionList  tom___optionList= (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch285_47) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom___codomain= tomMatch285_47.gettype() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch285_48) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___opName= tomMatch285_48.getname() ; tom.engine.adt.cst.types.CstSlotList  tom___slotList= (( tom.engine.adt.cst.types.CstBlock )cst).getargumentList() ; tom.engine.adt.cst.types.CstOperatorList  tom___operatorList= (( tom.engine.adt.cst.types.CstBlock )cst).getoperatorList() ;



        List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
        List<TomName> slotNameList = new LinkedList<TomName>();
        List<Option> options = new LinkedList<Option>();
        TomTypeList types =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;

        options.add(makeOriginTracking(tom___opName,tom___optionList));

        { /* unamed block */{ /* unamed block */if ( (tom___slotList instanceof tom.engine.adt.cst.types.CstSlotList) ) {if ( (((( tom.engine.adt.cst.types.CstSlotList )tom___slotList) instanceof tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot) || ((( tom.engine.adt.cst.types.CstSlotList )tom___slotList) instanceof tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot)) ) { tom.engine.adt.cst.types.CstSlotList  tomMatch289_end_4=(( tom.engine.adt.cst.types.CstSlotList )tom___slotList);do {{ /* unamed block */if (!( tomMatch289_end_4.isEmptyConcCstSlot() )) { tom.engine.adt.cst.types.CstSlot  tomMatch289_9= tomMatch289_end_4.getHeadConcCstSlot() ;if ( ((( tom.engine.adt.cst.types.CstSlot )tomMatch289_9) instanceof tom.engine.adt.cst.types.cstslot.Cst_Slot) ) { tom.engine.adt.cst.types.CstName  tomMatch289_7= tomMatch289_9.getslotName() ; tom.engine.adt.cst.types.CstType  tomMatch289_8= tomMatch289_9.getslotTypeName() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch289_7) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___slotName= tomMatch289_7.getname() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch289_8) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom___slotType= tomMatch289_8.gettype() ;



            TomName astName = ASTFactory.makeName(tom___slotName);
            if(slotNameList.indexOf(astName) != -1) {
              CstOption ot = getOriginTracking(tom___optionList);
              TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                  TomMessage.repeatedSlotName,
                  tom___slotName
);
            }
            slotNameList.add(astName);
            pairNameDeclList.add( tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make(astName,  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ) );
            types = tom_append_list_concTomType(types, tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(makeType(tom___slotType), tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) );
            String typeOfSlot = getSlotType(tom___codomain,tom___slotName);
            if(typeOfSlot != null && !typeOfSlot.equals(tom___slotType)) {
              CstOption ot = getOriginTracking(tom___optionList);
              TomMessage.warning(getLogger(),ot.getfileName(), ot.getstartLine(),
                  TomMessage.slotIncompatibleTypes,tom___slotName,tom___slotType,typeOfSlot);
            } else {
              putSlotType(tom___codomain,tom___slotName,tom___slotType);
            }

          }}}}if ( tomMatch289_end_4.isEmptyConcCstSlot() ) {tomMatch289_end_4=(( tom.engine.adt.cst.types.CstSlotList )tom___slotList);} else {tomMatch289_end_4= tomMatch289_end_4.getTailConcCstSlot() ;}}} while(!( (tomMatch289_end_4==(( tom.engine.adt.cst.types.CstSlotList )tom___slotList)) ));}}}}{ /* unamed block */{ /* unamed block */if ( (tom___operatorList instanceof tom.engine.adt.cst.types.CstOperatorList) ) {if ( (((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator) || ((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator)) ) { tom.engine.adt.cst.types.CstOperatorList  tomMatch290_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);do {{ /* unamed block */if (!( tomMatch290_end_4.isEmptyConcCstOperator() )) { tom.engine.adt.cst.types.CstOperator  tom___operator= tomMatch290_end_4.getHeadConcCstOperator() ;{ /* unamed block */{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_IsFsym) ) { tom.engine.adt.cst.types.CstName  tomMatch291_1= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch291_2= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch291_1) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch291_1.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_2) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_2) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch291_2.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch291_12= tomMatch291_2.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch291_12) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch291_12.getoptionList() ;if (  tomMatch291_2.getTailConcCstBlock() .isEmptyConcCstBlock() ) {








                String code = ASTFactory.abstractCode( tomMatch291_12.getcontent() ,tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 




;
                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_GetDefault) ) { tom.engine.adt.cst.types.CstName  tomMatch291_15= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch291_16= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch291_15) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___slotName= tomMatch291_15.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_16) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_16) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) { tom.engine.adt.cst.types.CstBlockList  tom___blockList=tomMatch291_16;if (!( tomMatch291_16.isEmptyConcCstBlock() )) {



                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ;
matchblock: {
                { /* unamed block */{ /* unamed block */if ( (tom___blockList instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch292_5= (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch292_5) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {if (  (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).getTailConcCstBlock() .isEmptyConcCstBlock() ) {


                    //System.out.println("keywordGetDefault: " + `content);
                    attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetDefaultDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) ,  tom.engine.adt.tomname.types.tomname.Name.make(tom___slotName) ,  tom.engine.adt.tomexpression.types.expression.Code.make( tomMatch292_5.getcontent() ) , makeOriginTracking(tom___opName, tomMatch292_5.getoptionList() )) 




;
                    break matchblock;
                  }}}}}}{ /* unamed block */if ( (tom___blockList instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).isEmptyConcCstBlock() )) {


                    InstructionList il = convert(tom___blockList);
                    attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetDefaultDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) ,  tom.engine.adt.tomname.types.tomname.Name.make(tom___slotName) ,  tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression.make( tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(il) ) , makeOriginTracking(tom___opName,tom___optionList)) 




;
                    break matchblock;
                  }}}}}


            }
            options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_GetSlot) ) { tom.engine.adt.cst.types.CstName  tomMatch291_26= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName1() ; tom.engine.adt.cst.types.CstName  tomMatch291_27= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName2() ; tom.engine.adt.cst.types.CstBlockList  tomMatch291_28= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch291_26) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch291_27) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___argName= tomMatch291_27.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_28) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_28) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch291_28.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch291_41= tomMatch291_28.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch291_41) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch291_41.getoptionList() ;if (  tomMatch291_28.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch291_41.getcontent() ,tom___argName);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) ,  tom.engine.adt.tomname.types.tomname.Name.make( tomMatch291_26.getname() ) , makeBQVariableFromName(tom___argName,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 





;

                TomName sName = attribute.getSlotName();
                int index = slotNameList.indexOf(sName);
                /*
                 * ensure that sName appears in slotNameList, only once
                 * ensure that sName has not already been generated
                 */
                TomMessage msg = null;
                if(index == -1) {
                  msg = TomMessage.errorIncompatibleSlotDecl;
                } else {
                  PairNameDecl pair = pairNameDeclList.get(index);
                  { /* unamed block */{ /* unamed block */if ( (pair instanceof tom.engine.adt.tomslot.types.PairNameDecl) ) {if ( ((( tom.engine.adt.tomslot.types.PairNameDecl )pair) instanceof tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl) ) {


                      if( (( tom.engine.adt.tomslot.types.PairNameDecl )pair).getSlotDecl()  !=  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ) {
                        msg = TomMessage.errorTwoSameSlotDecl;
                      }
                    }}}}


                }
                if(msg != null) {
                  CstOption ot = getOriginTracking(tom___optionList);
                  CstOption ot2 = getOriginTracking(tom___optionList2);
                  TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                      msg,
                      ot2.getfileName(), ot2.getstartLine(),
                      "%op " + tom___codomain, ot.getstartLine(),sName.getString());
                } else {
                  pairNameDeclList.set(index, tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make(sName, attribute) );
                }
              }}}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_Make) ) { tom.engine.adt.cst.types.CstBlockList  tomMatch291_45= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ; tom.engine.adt.cst.types.CstNameList  tom___nameList= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getnameList() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_45) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch291_45) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) { tom.engine.adt.cst.types.CstBlockList  tom___blockList=tomMatch291_45;if (!( tomMatch291_45.isEmptyConcCstBlock() )) {



                ArrayList<String> varnameList = new ArrayList<String>();
                BQTermList args =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
                int index = 0;

                { /* unamed block */{ /* unamed block */if ( (tom___nameList instanceof tom.engine.adt.cst.types.CstNameList) ) {if ( (((( tom.engine.adt.cst.types.CstNameList )tom___nameList) instanceof tom.engine.adt.cst.types.cstnamelist.ConsConcCstName) || ((( tom.engine.adt.cst.types.CstNameList )tom___nameList) instanceof tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName)) ) { tom.engine.adt.cst.types.CstNameList  tomMatch294_end_4=(( tom.engine.adt.cst.types.CstNameList )tom___nameList);do {{ /* unamed block */if (!( tomMatch294_end_4.isEmptyConcCstName() )) { tom.engine.adt.cst.types.CstName  tomMatch294_8= tomMatch294_end_4.getHeadConcCstName() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch294_8) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___argName= tomMatch294_8.getname() ;



                    varnameList.add(tom___argName);
                    TomType type = (types.length()>0)?TomBase.elementAt(types,index++): tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
                    args = tom_append_list_concBQTerm(args, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(tom___argName) , type) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
                  }}if ( tomMatch294_end_4.isEmptyConcCstName() ) {tomMatch294_end_4=(( tom.engine.adt.cst.types.CstNameList )tom___nameList);} else {tomMatch294_end_4= tomMatch294_end_4.getTailConcCstName() ;}}} while(!( (tomMatch294_end_4==(( tom.engine.adt.cst.types.CstNameList )tom___nameList)) ));}}}}



                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ;
matchblock: {
              { /* unamed block */{ /* unamed block */if ( (tom___blockList instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch295_5= (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch295_5) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {if (  (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).getTailConcCstBlock() .isEmptyConcCstBlock() ) {


                  String[] vars = new String[varnameList.size()]; // used only to give a type
                  String code = ASTFactory.abstractCode( tomMatch295_5.getcontent() ,varnameList.toArray(vars));
                  attribute =  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeType(tom___codomain), args,  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(code) ) , makeOriginTracking(tom___opName, tomMatch295_5.getoptionList() )) 





;
                  break matchblock;
                }}}}}}{ /* unamed block */if ( (tom___blockList instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )tom___blockList)) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( (( tom.engine.adt.cst.types.CstBlockList )tom___blockList).isEmptyConcCstBlock() )) {



                  InstructionList il = convert(tom___blockList);
                  //System.out.println("il: " + il);
                  attribute =  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeType(tom___codomain), args,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(il) , makeOriginTracking(tom___opName,tom___optionList)) 





;
                  break matchblock;
                }}}}}



            }

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}if ( tomMatch290_end_4.isEmptyConcCstOperator() ) {tomMatch290_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);} else {tomMatch290_end_4= tomMatch290_end_4.getTailConcCstOperator() ;}}} while(!( (tomMatch290_end_4==(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList)) ));}}}}






        TomSymbol astSymbol = ASTFactory.makeSymbol(tom___opName, makeType(tom___codomain), types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
        symbolTable.putSymbol(tom___opName,astSymbol);
        return  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.DeclarationToCode.make( tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) ) ) ) ;
      }}}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_OpArrayConstruct) ) { tom.engine.adt.cst.types.CstType  tomMatch285_61= (( tom.engine.adt.cst.types.CstBlock )cst).getcodomain() ; tom.engine.adt.cst.types.CstName  tomMatch285_62= (( tom.engine.adt.cst.types.CstBlock )cst).getctorName() ; tom.engine.adt.cst.types.CstType  tomMatch285_63= (( tom.engine.adt.cst.types.CstBlock )cst).getdomain() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch285_61) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom___codomain= tomMatch285_61.gettype() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch285_62) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___opName= tomMatch285_62.getname() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch285_63) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom___domain= tomMatch285_63.gettype() ; tom.engine.adt.cst.types.CstOperatorList  tom___operatorList= (( tom.engine.adt.cst.types.CstBlock )cst).getoperatorList() ;



        List<Option> options = new LinkedList<Option>();
        options.add(makeOriginTracking(tom___opName, (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() ));

        { /* unamed block */{ /* unamed block */if ( (tom___operatorList instanceof tom.engine.adt.cst.types.CstOperatorList) ) {if ( (((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator) || ((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator)) ) { tom.engine.adt.cst.types.CstOperatorList  tomMatch296_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);do {{ /* unamed block */if (!( tomMatch296_end_4.isEmptyConcCstOperator() )) { tom.engine.adt.cst.types.CstOperator  tom___operator= tomMatch296_end_4.getHeadConcCstOperator() ;{ /* unamed block */{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_IsFsym) ) { tom.engine.adt.cst.types.CstName  tomMatch297_1= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch297_2= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_1) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch297_1.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_2) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_2) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch297_2.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch297_12= tomMatch297_2.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch297_12) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch297_12.getoptionList() ;if (  tomMatch297_2.getTailConcCstBlock() .isEmptyConcCstBlock() ) {





                String code = ASTFactory.abstractCode( tomMatch297_12.getcontent() ,tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 




;
                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyArray) ) { tom.engine.adt.cst.types.CstName  tomMatch297_15= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch297_16= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_15) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch297_15.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_16) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_16) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch297_16.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch297_26= tomMatch297_16.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch297_26) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch297_26.getoptionList() ;if (  tomMatch297_16.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch297_26.getcontent() , tom___name);
                Instruction instruction =  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(code) ) ;
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2), instruction, makeOriginTracking(tom___opName,tom___optionList2)) 




;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_MakeAppend) ) { tom.engine.adt.cst.types.CstName  tomMatch297_29= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName1() ; tom.engine.adt.cst.types.CstName  tomMatch297_30= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName2() ; tom.engine.adt.cst.types.CstBlockList  tomMatch297_31= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_29) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name1= tomMatch297_29.getname() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_30) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name2= tomMatch297_30.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_31) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_31) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch297_31.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch297_44= tomMatch297_31.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch297_44) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch297_44.getoptionList() ;if (  tomMatch297_31.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch297_44.getcontent() , tom___name1, tom___name2);
                Instruction instruction =  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(code) ) ;
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name1,tom___domain,tom___optionList2), makeBQVariableFromName(tom___name2,tom___codomain,tom___optionList2), instruction, makeOriginTracking(tom___opName,tom___optionList2)) 





;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_GetSize) ) { tom.engine.adt.cst.types.CstName  tomMatch297_47= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch297_48= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_47) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch297_47.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_48) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_48) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch297_48.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch297_58= tomMatch297_48.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch297_58) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch297_58.getoptionList() ;if (  tomMatch297_48.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch297_58.getcontent() , tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 




;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_GetElement) ) { tom.engine.adt.cst.types.CstName  tomMatch297_61= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName1() ; tom.engine.adt.cst.types.CstName  tomMatch297_62= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName2() ; tom.engine.adt.cst.types.CstBlockList  tomMatch297_63= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_61) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name1= tomMatch297_61.getname() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_62) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name2= tomMatch297_62.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_63) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch297_63) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch297_63.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch297_76= tomMatch297_63.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch297_76) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch297_76.getoptionList() ;if (  tomMatch297_63.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch297_76.getcontent() , tom___name1, tom___name2);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name1,tom___codomain,tom___optionList2), makeBQVariableFromName(tom___name2,"int",tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 





;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}}}}if ( tomMatch296_end_4.isEmptyConcCstOperator() ) {tomMatch296_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);} else {tomMatch296_end_4= tomMatch296_end_4.getTailConcCstOperator() ;}}} while(!( (tomMatch296_end_4==(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList)) ));}}}}





        PairNameDeclList pairNameDeclList =  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make( tom.engine.adt.tomname.types.tomname.EmptyName.make() ,  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ) , tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) ;
        TomTypeList types =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(makeType(tom___domain), tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) ;
        TomSymbol astSymbol = ASTFactory.makeSymbol(tom___opName, makeType(tom___codomain), types, pairNameDeclList, options);
        symbolTable.putSymbol(tom___opName,astSymbol);
        return  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.DeclarationToCode.make( tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) ) ) ) ;
      }}}}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_OpListConstruct) ) { tom.engine.adt.cst.types.CstType  tomMatch285_78= (( tom.engine.adt.cst.types.CstBlock )cst).getcodomain() ; tom.engine.adt.cst.types.CstName  tomMatch285_79= (( tom.engine.adt.cst.types.CstBlock )cst).getctorName() ; tom.engine.adt.cst.types.CstType  tomMatch285_80= (( tom.engine.adt.cst.types.CstBlock )cst).getdomain() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch285_78) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom___codomain= tomMatch285_78.gettype() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch285_79) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___opName= tomMatch285_79.getname() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch285_80) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom___domain= tomMatch285_80.gettype() ; tom.engine.adt.cst.types.CstOperatorList  tom___operatorList= (( tom.engine.adt.cst.types.CstBlock )cst).getoperatorList() ;



        List<Option> options = new LinkedList<Option>();
        options.add(makeOriginTracking(tom___opName, (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() ));

        { /* unamed block */{ /* unamed block */if ( (tom___operatorList instanceof tom.engine.adt.cst.types.CstOperatorList) ) {if ( (((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator) || ((( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList) instanceof tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator)) ) { tom.engine.adt.cst.types.CstOperatorList  tomMatch298_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);do {{ /* unamed block */if (!( tomMatch298_end_4.isEmptyConcCstOperator() )) { tom.engine.adt.cst.types.CstOperator  tom___operator= tomMatch298_end_4.getHeadConcCstOperator() ;{ /* unamed block */{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_IsFsym) ) { tom.engine.adt.cst.types.CstName  tomMatch299_1= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch299_2= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch299_1) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch299_1.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_2) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_2) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch299_2.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch299_12= tomMatch299_2.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch299_12) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch299_12.getoptionList() ;if (  tomMatch299_2.getTailConcCstBlock() .isEmptyConcCstBlock() ) {





                String code = ASTFactory.abstractCode( tomMatch299_12.getcontent() ,tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 




;
                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyList) ) { tom.engine.adt.cst.types.CstBlockList  tomMatch299_15= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_15) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_15) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch299_15.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch299_22= tomMatch299_15.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch299_22) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {if (  tomMatch299_15.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code =  tomMatch299_22.getcontent() ;
                Instruction instruction =  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(code) ) ;
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , instruction, makeOriginTracking(tom___opName, tomMatch299_22.getoptionList() )) 



;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_MakeInsert) ) { tom.engine.adt.cst.types.CstName  tomMatch299_25= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName1() ; tom.engine.adt.cst.types.CstName  tomMatch299_26= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName2() ; tom.engine.adt.cst.types.CstBlockList  tomMatch299_27= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch299_25) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name1= tomMatch299_25.getname() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch299_26) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name2= tomMatch299_26.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_27) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_27) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch299_27.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch299_40= tomMatch299_27.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch299_40) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch299_40.getoptionList() ;if (  tomMatch299_27.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch299_40.getcontent() , tom___name1, tom___name2);
                Instruction instruction =  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(code) ) ;
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.MakeAddList.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name1,tom___domain,tom___optionList2), makeBQVariableFromName(tom___name2,tom___codomain,tom___optionList2), instruction, makeOriginTracking(tom___opName,tom___optionList2)) 





;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_GetHead) ) { tom.engine.adt.cst.types.CstName  tomMatch299_43= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch299_44= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch299_43) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch299_43.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_44) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_44) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch299_44.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch299_54= tomMatch299_44.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch299_54) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch299_54.getoptionList() ;if (  tomMatch299_44.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch299_54.getcontent() , tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , symbolTable.getUniversalType(), makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 





;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_GetTail) ) { tom.engine.adt.cst.types.CstName  tomMatch299_57= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch299_58= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch299_57) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch299_57.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_58) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_58) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch299_58.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch299_68= tomMatch299_58.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch299_68) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch299_68.getoptionList() ;if (  tomMatch299_58.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch299_68.getcontent() , tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 




;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}{ /* unamed block */if ( (tom___operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom___operator) instanceof tom.engine.adt.cst.types.cstoperator.Cst_IsEmpty) ) { tom.engine.adt.cst.types.CstName  tomMatch299_71= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch299_72= (( tom.engine.adt.cst.types.CstOperator )tom___operator).getblock() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch299_71) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch299_71.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_72) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch299_72) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch299_72.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch299_82= tomMatch299_72.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch299_82) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom___optionList2= tomMatch299_82.getoptionList() ;if (  tomMatch299_72.getTailConcCstBlock() .isEmptyConcCstBlock() ) {



                String code = ASTFactory.abstractCode( tomMatch299_82.getcontent() , tom___name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) , makeBQVariableFromName(tom___name,tom___codomain,tom___optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) , makeOriginTracking(tom___opName,tom___optionList2)) 




;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}}}if ( tomMatch298_end_4.isEmptyConcCstOperator() ) {tomMatch298_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList);} else {tomMatch298_end_4= tomMatch298_end_4.getTailConcCstOperator() ;}}} while(!( (tomMatch298_end_4==(( tom.engine.adt.cst.types.CstOperatorList )tom___operatorList)) ));}}}}






        PairNameDeclList pairNameDeclList =  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make( tom.engine.adt.tomname.types.tomname.EmptyName.make() ,  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ) , tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) ;
        TomTypeList types =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(makeType(tom___domain), tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) ;
        TomSymbol astSymbol = ASTFactory.makeSymbol(tom___opName, makeType(tom___codomain), types, pairNameDeclList, options);
        symbolTable.putSymbol(tom___opName,astSymbol);

        return  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.DeclarationToCode.make( tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___opName) ) ) ) ;
      }}}}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_IncludeConstruct) ) {



        return  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TomInclude.make(convertToCodeList( (( tom.engine.adt.cst.types.CstBlock )cst).getblocks() )) ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_AbstractBlock) ) {



        return  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getblocks() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.cstblock.Cst_UnamedBlock) ) {



        return  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getblocks() )) ;
      }}}}


 // end %match
   
    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTerm convert(CstBQTerm cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQDefault) ) {


 
        return  tom.engine.adt.code.types.bqterm.BQDefault.make() ; 
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQAppl) ) { String  tom___name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;



        return  tom.engine.adt.code.types.bqterm.BQAppl.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom___name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) , convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getbqTermList() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQRecordAppl) ) { String  tom___name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;



        return  tom.engine.adt.code.types.bqterm.BQRecordAppl.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom___name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) , convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getslotList() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQVar) ) { String  tom___name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;if ( ((( tom.engine.adt.cst.types.CstType ) (( tom.engine.adt.cst.types.CstBQTerm )cst).gettype() ) instanceof tom.engine.adt.cst.types.csttype.Cst_TypeUnknown) ) {



        return  tom.engine.adt.code.types.bqterm.BQVariable.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom___name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) , SymbolTable.TYPE_UNKNOWN) ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQVar) ) { tom.engine.adt.cst.types.CstType  tomMatch300_26= (( tom.engine.adt.cst.types.CstBQTerm )cst).gettype() ; String  tom___name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch300_26) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {



        return  tom.engine.adt.code.types.bqterm.BQVariable.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom___name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) , makeType( tomMatch300_26.gettype() )) ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQVarStar) ) { String  tom___name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;if ( ((( tom.engine.adt.cst.types.CstType ) (( tom.engine.adt.cst.types.CstBQTerm )cst).gettype() ) instanceof tom.engine.adt.cst.types.csttype.Cst_TypeUnknown) ) {



        return  tom.engine.adt.code.types.bqterm.BQVariableStar.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom___name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) , SymbolTable.TYPE_UNKNOWN) ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQConstant) ) {



        return  tom.engine.adt.code.types.bqterm.BuildConstant.make( tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ) ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_ITL) ) {



        return  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make( (( tom.engine.adt.cst.types.CstBQTerm )cst).getcode() ) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQUnderscore) ) {



        return  tom.engine.adt.code.types.bqterm.BQDefault.make() ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite) ) { tom.engine.adt.cst.types.CstBQTermList  tom___argList= (( tom.engine.adt.cst.types.CstBQTerm )cst).getlist() ;



        BQTerm composite =  tom.engine.adt.code.types.bqterm.EmptyComposite.make() ;
        { /* unamed block */{ /* unamed block */if ( (tom___argList instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )tom___argList) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )tom___argList) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) { tom.engine.adt.cst.types.CstBQTermList  tomMatch301_end_4=(( tom.engine.adt.cst.types.CstBQTermList )tom___argList);do {{ /* unamed block */if (!( tomMatch301_end_4.isEmptyConcCstBQTerm() )) { tom.engine.adt.cst.types.CstBQTerm  tom___bqterm= tomMatch301_end_4.getHeadConcCstBQTerm() ;{ /* unamed block */{ /* unamed block */if ( (tom___bqterm instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )tom___bqterm) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_ITL) ) {



 composite = tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make( (( tom.engine.adt.cst.types.CstBQTerm )tom___bqterm).getcode() ) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ); }}}{ /* unamed block */if ( (tom___bqterm instanceof tom.engine.adt.cst.types.CstBQTerm) ) {boolean tomMatch302_8= false ;if ( ((( tom.engine.adt.cst.types.CstBQTerm )tom___bqterm) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_ITL) ) {tomMatch302_8= true ;}if (!(tomMatch302_8)) {
 composite = tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(convert(tom___bqterm)) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ); }}}}}if ( tomMatch301_end_4.isEmptyConcCstBQTerm() ) {tomMatch301_end_4=(( tom.engine.adt.cst.types.CstBQTermList )tom___argList);} else {tomMatch301_end_4= tomMatch301_end_4.getTailConcCstBQTerm() ;}}} while(!( (tomMatch301_end_4==(( tom.engine.adt.cst.types.CstBQTermList )tom___argList)) ));}}}}



        return composite;
      }}}}



    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTerm convert(CstTerm cst) {
    OptionList ol = addDefaultModule( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() );
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstTerm) ) {if ( ((( tom.engine.adt.cst.types.CstTerm )cst) instanceof tom.engine.adt.cst.types.cstterm.Cst_TermAppl) ) {


        return  tom.engine.adt.code.types.bqterm.BQAppl.make(ol,  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstTerm )cst).getname() ) , convert( (( tom.engine.adt.cst.types.CstTerm )cst).gettermList() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstTerm) ) {if ( ((( tom.engine.adt.cst.types.CstTerm )cst) instanceof tom.engine.adt.cst.types.cstterm.Cst_TermVariable) ) {



        return  tom.engine.adt.code.types.bqterm.BQVariable.make(ol,  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstTerm )cst).getname() ) , SymbolTable.TYPE_UNKNOWN) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstTerm) ) {if ( ((( tom.engine.adt.cst.types.CstTerm )cst) instanceof tom.engine.adt.cst.types.cstterm.Cst_TermVariableStar) ) {



        return  tom.engine.adt.code.types.bqterm.BQVariableStar.make(ol,  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstTerm )cst).getname() ) , SymbolTable.TYPE_UNKNOWN) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstTerm) ) {if ( ((( tom.engine.adt.cst.types.CstTerm )cst) instanceof tom.engine.adt.cst.types.cstterm.Cst_TermConstant) ) {



        return  tom.engine.adt.code.types.bqterm.BuildConstant.make( tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstTerm )cst).getname() ) ) ;
      }}}}








    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQSlot convert(CstPairSlotBQTerm cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPairSlotBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstPairSlotBQTerm )cst) instanceof tom.engine.adt.cst.types.cstpairslotbqterm.Cst_PairSlotBQTerm) ) { tom.engine.adt.cst.types.CstName  tomMatch304_2= (( tom.engine.adt.cst.types.CstPairSlotBQTerm )cst).getslotName() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch304_2) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) {


        return  tom.engine.adt.tomslot.types.bqslot.PairSlotBQTerm.make( tom.engine.adt.tomname.types.tomname.Name.make( tomMatch304_2.getname() ) , convert( (( tom.engine.adt.cst.types.CstPairSlotBQTerm )cst).getterm() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Option convert(CstOption cst, String subject) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstOption) ) {if ( ((( tom.engine.adt.cst.types.CstOption )cst) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {


        return  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(subject) ,  (( tom.engine.adt.cst.types.CstOption )cst).getstartLine() ,  (( tom.engine.adt.cst.types.CstOption )cst).getfileName() ) ;
      }}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public ConstraintInstruction convert(CstConstraintAction cst, CstBQTermList subjectList) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraintAction) ) {if ( ((( tom.engine.adt.cst.types.CstConstraintAction )cst) instanceof tom.engine.adt.cst.types.cstconstraintaction.Cst_ConstraintAction) ) {


        int currentIndex = 0; // index of the current subject of subjectList
        OptionList newoptionList = convert( (( tom.engine.adt.cst.types.CstConstraintAction )cst).getoptionList() ,"ConstraintAction");
        InstructionList instructionList = convert( (( tom.engine.adt.cst.types.CstConstraintAction )cst).getaction() );

        return  tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(convert( (( tom.engine.adt.cst.types.CstConstraintAction )cst).getconstraint() ,subjectList,currentIndex),  tom.engine.adt.tominstruction.types.instruction.RawAction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.TrueTL.make() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructionList) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ) , newoptionList) 


;
      }}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Constraint convert(CstConstraint cst, CstBQTermList subjectList, int subjectIndex) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_MatchArgumentConstraint) ) {


        CstBQTermList l = subjectList;
        if (subjectIndex < 0 || subjectIndex > subjectList.length()) {
          throw new IllegalArgumentException("illegal list index: " + subjectIndex);
        }
        for(int i = 0 ; i < subjectIndex ; i++) {
          l = l.getTailConcCstBQTerm();
        }
        BQTerm currentSubject = convert(l.getHeadConcCstBQTerm());
        TomType type = getTomType(currentSubject);

        //System.out.println("pattern = " + `pattern);
        //System.out.println("subjectIndex = " + subjectIndex);
        //System.out.println("currentSubject = " + currentSubject);
        //System.out.println("type = " + type);

        return  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getpattern() ), currentSubject, type) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_MatchTermConstraint) ) { tom.engine.adt.cst.types.CstType  tom___typeConstraint= (( tom.engine.adt.cst.types.CstConstraint )cst).gettype() ;



        BQTerm currentSubject = convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getsubject() );
        TomType type = null;
        { /* unamed block */{ /* unamed block */if ( (tom___typeConstraint instanceof tom.engine.adt.cst.types.CstType) ) {

 type = getTomType(currentSubject); }}{ /* unamed block */if ( (tom___typeConstraint instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tom___typeConstraint) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {
 
            TomSymbol symbol = symbolTable.getSymbolFromName( (( tom.engine.adt.cst.types.CstType )tom___typeConstraint).gettype() );
            if(symbol != null) {
              type = symbol.getTypesToType().getCodomain();
            }
          }}}}


        if(type != null) {
          return  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getpattern() ), currentSubject, type) ;
        }
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {if ( (  (( tom.engine.adt.cst.types.CstConstraint )cst).isEmptyCst_AndConstraint()  ||  ((( tom.engine.adt.cst.types.CstConstraint )cst)== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() )  ) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {if (!( (  (( tom.engine.adt.cst.types.CstConstraint )cst).isEmptyCst_AndConstraint()  ||  ((( tom.engine.adt.cst.types.CstConstraint )cst)== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() )  ) )) {



        Constraint chead = convert((( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )?( (( tom.engine.adt.cst.types.CstConstraint )cst).getHeadCst_AndConstraint() ):((( tom.engine.adt.cst.types.CstConstraint )cst))),subjectList,subjectIndex);
        Constraint ctail = convert((( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )?( (( tom.engine.adt.cst.types.CstConstraint )cst).getTailCst_AndConstraint() ):( tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() )),subjectList,subjectIndex+1);
        return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(chead, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(ctail, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ; 
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {if ( (  (( tom.engine.adt.cst.types.CstConstraint )cst).isEmptyCst_OrConstraint()  ||  ((( tom.engine.adt.cst.types.CstConstraint )cst)== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() )  ) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.FalseConstraint.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {if (!( (  (( tom.engine.adt.cst.types.CstConstraint )cst).isEmptyCst_OrConstraint()  ||  ((( tom.engine.adt.cst.types.CstConstraint )cst)== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() )  ) )) {



        Constraint chead = convert((( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )?( (( tom.engine.adt.cst.types.CstConstraint )cst).getHeadCst_OrConstraint() ):((( tom.engine.adt.cst.types.CstConstraint )cst))),subjectList,subjectIndex);
        Constraint ctail = convert((( (((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )?( (( tom.engine.adt.cst.types.CstConstraint )cst).getTailCst_OrConstraint() ):( tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() )),subjectList,subjectIndex);
        return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(chead, tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(ctail, tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) ) ; 
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_NumLessThan) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getlefthand() ), convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getrighthand() ),  tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessThan.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_NumLessOrEqualThan) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getlefthand() ), convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getrighthand() ),  tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessOrEqualThan.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_NumGreaterThan) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getlefthand() ), convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getrighthand() ),  tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterThan.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_NumGreaterOrEqualThan) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getlefthand() ), convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getrighthand() ),  tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterOrEqualThan.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_EqualTo) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getlefthand() ), convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getrighthand() ),  tom.engine.adt.tomconstraint.types.numericconstrainttype.NumEqual.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_Different) ) {



        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getlefthand() ), convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getrighthand() ),  tom.engine.adt.tomconstraint.types.numericconstrainttype.NumDifferent.make() ) ;
      }}}}



    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomTerm convert(CstPattern cst) {
    // TODO: define option
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Variable) ) {


        return  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstPattern )cst).getname() ) , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_VariableStar) ) {



        return  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstPattern )cst).getname() ) , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_UnamedVariable) ) {



        return  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.EmptyName.make() , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_UnamedVariableStar) ) {



        return  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.EmptyName.make() , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Constant) ) {



        OptionList optionList =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
        ConstraintList constraintList =  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ;
        TomName name = convert( (( tom.engine.adt.cst.types.CstPattern )cst).getsymbol() ); // add symbol to symbolTable
        return  tom.engine.adt.tomterm.types.tomterm.TermAppl.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(name, tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ,  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_ConstantOr) ) {



        OptionList optionList =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
        TomNameList nameList = convert( (( tom.engine.adt.cst.types.CstPattern )cst).getsymbolList() ); 
        return  tom.engine.adt.tomterm.types.tomterm.TermAppl.make(optionList, nameList,  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Anti) ) {












        return  tom.engine.adt.tomterm.types.tomterm.AntiTerm.make(convert( (( tom.engine.adt.cst.types.CstPattern )cst).getpattern() )) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Appl) ) { tom.engine.adt.cst.types.CstSymbolList  tom___symbolList= (( tom.engine.adt.cst.types.CstPattern )cst).getheadSymbolList() ;



        OptionList optionList =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
        Theory theory = extractTheory(tom___symbolList);
        if(theory.length() > 0) {
          optionList = tom_append_list_concOption(optionList, tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.MatchingTheory.make(theory) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) );
        }
        TomNameList nameList = convert(tom___symbolList); 
        TomList argList = convert( (( tom.engine.adt.cst.types.CstPattern )cst).getpatternList() ); 
        ConstraintList constraintList =  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ;
        return  tom.engine.adt.tomterm.types.tomterm.TermAppl.make(optionList, nameList, argList, constraintList) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_RecordAppl) ) { tom.engine.adt.cst.types.CstSymbolList  tom___symbolList= (( tom.engine.adt.cst.types.CstPattern )cst).getheadSymbolList() ;



        OptionList optionList =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
        Theory theory = extractTheory(tom___symbolList);
        if(theory.length() > 0) {
          optionList = tom_append_list_concOption(optionList, tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.MatchingTheory.make(theory) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) );
        }
        TomNameList nameList = convert(tom___symbolList); 
        SlotList slotList = convert( (( tom.engine.adt.cst.types.CstPattern )cst).getpairPatternList() ); 
        ConstraintList constraintList =  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ;
        return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(optionList, nameList, slotList, constraintList) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.cstpattern.Cst_AnnotatedPattern) ) {



        TomTerm pattern = convert( (( tom.engine.adt.cst.types.CstPattern )cst).getpattern() );
        int line = 0;
        Constraint constraint =  ASTFactory.makeAliasTo( tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstPattern )cst).getannotation() ) , line, "unknown file");
        { /* unamed block */{ /* unamed block */if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch310_7= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch310_4= null ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch310_1= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch310_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch310_6= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch310_5= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{ /* unamed block */tomMatch310_7= true ;tomMatch310_3=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch310_1= tomMatch310_3.getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{ /* unamed block */tomMatch310_7= true ;tomMatch310_4=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch310_1= tomMatch310_4.getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch310_7= true ;tomMatch310_5=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch310_1= tomMatch310_5.getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch310_7= true ;tomMatch310_6=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch310_1= tomMatch310_6.getConstraints() ;}}}}}if (tomMatch310_7) {if ( (tomMatch310_1 instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {


            return pattern.setConstraints( tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make(constraint,tom_append_list_concConstraint(tomMatch310_1, tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() )) );
          }}}}{ /* unamed block */if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tom___t= (( tom.engine.adt.tomterm.types.TomTerm )pattern).getTomTerm() ;



            ConstraintList constraints = tom___t.getConstraints();
            return  tom.engine.adt.tomterm.types.tomterm.AntiTerm.make(tom___t.setConstraints( tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make(constraint,tom_append_list_concConstraint(constraints, tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() )) )) ;
          }}}}}}}}




    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Slot convert(CstPairPattern cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPairPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPairPattern )cst) instanceof tom.engine.adt.cst.types.cstpairpattern.Cst_PairPattern) ) {


        return  tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstPairPattern )cst).getslotName() ) , convert( (( tom.engine.adt.cst.types.CstPairPattern )cst).getpattern() )) ;
      }}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomVisit convert(CstVisit cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstVisit) ) {if ( ((( tom.engine.adt.cst.types.CstVisit )cst) instanceof tom.engine.adt.cst.types.cstvisit.Cst_VisitTerm) ) { tom.engine.adt.cst.types.CstType  tomMatch312_1= (( tom.engine.adt.cst.types.CstVisit )cst).gettype() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch312_1) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {


        //TomType matchType = getOptionBooleanValue("newtyper"?SymbolTable.TYPE_UNKNOWN:type);
        // !! the name tom__arg is fixed
        CstBQTermList arguments =  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_BQVar.make( tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() , "tom__arg", tomMatch312_1) , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) ;
        return  tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make(makeType( tomMatch312_1.gettype() ), convert( (( tom.engine.adt.cst.types.CstVisit )cst).getconstraintActionList() ,arguments), convert( (( tom.engine.adt.cst.types.CstVisit )cst).getoptionList() ,"VisitTerm")) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  /*
   * get the value of a constant
   * and add the constant into the SymbolTable
   */
  public TomName convert(CstSymbol cst) {
    List optionList = new LinkedList();
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_Symbol) ) {


        return  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstSymbol )cst).getname() ) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_SymbolInt) ) { String  tom___name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;



        ASTFactory.makeIntegerSymbol(symbolTable,tom___name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_SymbolLong) ) { String  tom___name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;


        ASTFactory.makeLongSymbol(symbolTable,tom___name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_SymbolChar) ) { String  tom___name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;


        ASTFactory.makeCharSymbol(symbolTable,tom___name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_SymbolDouble) ) { String  tom___name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;


        ASTFactory.makeDoubleSymbol(symbolTable,tom___name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) ;
      }}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_SymbolString) ) { String  tom___name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;


        ASTFactory.makeStringSymbol(symbolTable,tom___name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom___name) ;
      }}}}




    throw new TomRuntimeException("convert: strange term: " + cst);
  }


  /*
   * List conversion
   */

  public CodeList convertToCodeList(CstBlockList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if ( (( tom.engine.adt.cst.types.CstBlockList )cst).isEmptyConcCstBlock() ) {

 
        return  tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( (( tom.engine.adt.cst.types.CstBlockList )cst).isEmptyConcCstBlock() )) {


        return  tom.engine.adt.code.types.codelist.ConsconcCode.make( tom.engine.adt.code.types.code.InstructionToCode.make(convert( (( tom.engine.adt.cst.types.CstBlockList )cst).getHeadConcCstBlock() )) ,tom_append_list_concCode(convertToCodeList( (( tom.engine.adt.cst.types.CstBlockList )cst).getTailConcCstBlock() ), tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }
 
  public InstructionList convert(CstBlockList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if ( (( tom.engine.adt.cst.types.CstBlockList )cst).isEmptyConcCstBlock() ) {

 
        return  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )cst) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( (( tom.engine.adt.cst.types.CstBlockList )cst).isEmptyConcCstBlock() )) {


        return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(convert( (( tom.engine.adt.cst.types.CstBlockList )cst).getHeadConcCstBlock() ),tom_append_list_concInstruction(convert( (( tom.engine.adt.cst.types.CstBlockList )cst).getTailConcCstBlock() ), tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTermList convert(CstBQTermList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )cst) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )cst) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) {if ( (( tom.engine.adt.cst.types.CstBQTermList )cst).isEmptyConcCstBQTerm() ) {

 
        return  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )cst) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )cst) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) {if (!( (( tom.engine.adt.cst.types.CstBQTermList )cst).isEmptyConcCstBQTerm() )) {


        return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(convert( (( tom.engine.adt.cst.types.CstBQTermList )cst).getHeadConcCstBQTerm() ),tom_append_list_concBQTerm(convert( (( tom.engine.adt.cst.types.CstBQTermList )cst).getTailConcCstBQTerm() ), tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTermList convert(CstTermList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstTermList) ) {if ( (((( tom.engine.adt.cst.types.CstTermList )cst) instanceof tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm) || ((( tom.engine.adt.cst.types.CstTermList )cst) instanceof tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm)) ) {if ( (( tom.engine.adt.cst.types.CstTermList )cst).isEmptyConcCstTerm() ) {

 
        return  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstTermList) ) {if ( (((( tom.engine.adt.cst.types.CstTermList )cst) instanceof tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm) || ((( tom.engine.adt.cst.types.CstTermList )cst) instanceof tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm)) ) {if (!( (( tom.engine.adt.cst.types.CstTermList )cst).isEmptyConcCstTerm() )) {


        return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(convert( (( tom.engine.adt.cst.types.CstTermList )cst).getHeadConcCstTerm() ),tom_append_list_concBQTerm(convert( (( tom.engine.adt.cst.types.CstTermList )cst).getTailConcCstTerm() ), tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQSlotList convert(CstPairSlotBQTermList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPairSlotBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm) || ((( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm)) ) {if ( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).isEmptyConcCstPairSlotBQTerm() ) {

 
        return  tom.engine.adt.tomslot.types.bqslotlist.EmptyconcBQSlot.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPairSlotBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm) || ((( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm)) ) {if (!( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).isEmptyConcCstPairSlotBQTerm() )) {


        return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make(convert( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).getHeadConcCstPairSlotBQTerm() ),tom_append_list_concBQSlot(convert( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).getTailConcCstPairSlotBQTerm() ), tom.engine.adt.tomslot.types.bqslotlist.EmptyconcBQSlot.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public OptionList convert(CstOptionList cst, String subject) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )cst) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )cst) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if ( (( tom.engine.adt.cst.types.CstOptionList )cst).isEmptyConcCstOption() ) {

 
        return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )cst) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )cst) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( (( tom.engine.adt.cst.types.CstOptionList )cst).isEmptyConcCstOption() )) {


        OptionList ol = convert( (( tom.engine.adt.cst.types.CstOptionList )cst).getTailConcCstOption() ,subject);
        return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(convert( (( tom.engine.adt.cst.types.CstOptionList )cst).getHeadConcCstOption() ,subject),tom_append_list_concOption(ol, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public ConstraintInstructionList convert(CstConstraintActionList cst, CstBQTermList subjectList) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraintActionList) ) {if ( (((( tom.engine.adt.cst.types.CstConstraintActionList )cst) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction) || ((( tom.engine.adt.cst.types.CstConstraintActionList )cst) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction)) ) {if ( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).isEmptyConcCstConstraintAction() ) {

 
        return  tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstConstraintActionList) ) {if ( (((( tom.engine.adt.cst.types.CstConstraintActionList )cst) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction) || ((( tom.engine.adt.cst.types.CstConstraintActionList )cst) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction)) ) {if (!( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).isEmptyConcCstConstraintAction() )) {


        ConstraintInstructionList ol = convert( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).getTailConcCstConstraintAction() ,subjectList);
        return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make(convert( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).getHeadConcCstConstraintAction() ,subjectList),tom_append_list_concConstraintInstruction(ol, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomNameList convert(CstSymbolList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbolList) ) {if ( (((( tom.engine.adt.cst.types.CstSymbolList )cst) instanceof tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol) || ((( tom.engine.adt.cst.types.CstSymbolList )cst) instanceof tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol)) ) {if ( (( tom.engine.adt.cst.types.CstSymbolList )cst).isEmptyConcCstSymbol() ) {

 
        return  tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstSymbolList) ) {if ( (((( tom.engine.adt.cst.types.CstSymbolList )cst) instanceof tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol) || ((( tom.engine.adt.cst.types.CstSymbolList )cst) instanceof tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol)) ) {if (!( (( tom.engine.adt.cst.types.CstSymbolList )cst).isEmptyConcCstSymbol() )) {


        return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(convert( (( tom.engine.adt.cst.types.CstSymbolList )cst).getHeadConcCstSymbol() ),tom_append_list_concTomName(convert( (( tom.engine.adt.cst.types.CstSymbolList )cst).getTailConcCstSymbol() ), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomList convert(CstPatternList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPatternList) ) {if ( (((( tom.engine.adt.cst.types.CstPatternList )cst) instanceof tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern) || ((( tom.engine.adt.cst.types.CstPatternList )cst) instanceof tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern)) ) {if ( (( tom.engine.adt.cst.types.CstPatternList )cst).isEmptyConcCstPattern() ) {

 
        return  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPatternList) ) {if ( (((( tom.engine.adt.cst.types.CstPatternList )cst) instanceof tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern) || ((( tom.engine.adt.cst.types.CstPatternList )cst) instanceof tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern)) ) {if (!( (( tom.engine.adt.cst.types.CstPatternList )cst).isEmptyConcCstPattern() )) {


        return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(convert( (( tom.engine.adt.cst.types.CstPatternList )cst).getHeadConcCstPattern() ),tom_append_list_concTomTerm(convert( (( tom.engine.adt.cst.types.CstPatternList )cst).getTailConcCstPattern() ), tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public SlotList convert(CstPairPatternList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPairPatternList) ) {if ( (((( tom.engine.adt.cst.types.CstPairPatternList )cst) instanceof tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern) || ((( tom.engine.adt.cst.types.CstPairPatternList )cst) instanceof tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern)) ) {if ( (( tom.engine.adt.cst.types.CstPairPatternList )cst).isEmptyConcCstPairPattern() ) {

 
        return  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstPairPatternList) ) {if ( (((( tom.engine.adt.cst.types.CstPairPatternList )cst) instanceof tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern) || ((( tom.engine.adt.cst.types.CstPairPatternList )cst) instanceof tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern)) ) {if (!( (( tom.engine.adt.cst.types.CstPairPatternList )cst).isEmptyConcCstPairPattern() )) {


        return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(convert( (( tom.engine.adt.cst.types.CstPairPatternList )cst).getHeadConcCstPairPattern() ),tom_append_list_concSlot(convert( (( tom.engine.adt.cst.types.CstPairPatternList )cst).getTailConcCstPairPattern() ), tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomVisitList convert(CstVisitList cst) {
    { /* unamed block */{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstVisitList) ) {if ( (((( tom.engine.adt.cst.types.CstVisitList )cst) instanceof tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit) || ((( tom.engine.adt.cst.types.CstVisitList )cst) instanceof tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit)) ) {if ( (( tom.engine.adt.cst.types.CstVisitList )cst).isEmptyConcCstVisit() ) {

 
        return  tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() ;
      }}}}{ /* unamed block */if ( (cst instanceof tom.engine.adt.cst.types.CstVisitList) ) {if ( (((( tom.engine.adt.cst.types.CstVisitList )cst) instanceof tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit) || ((( tom.engine.adt.cst.types.CstVisitList )cst) instanceof tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit)) ) {if (!( (( tom.engine.adt.cst.types.CstVisitList )cst).isEmptyConcCstVisit() )) {


        return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make(convert( (( tom.engine.adt.cst.types.CstVisitList )cst).getHeadConcCstVisit() ),tom_append_list_concTomVisit(convert( (( tom.engine.adt.cst.types.CstVisitList )cst).getTailConcCstVisit() ), tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() )) ;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }
  /*
   * Utilities
   */
  private CstOption getOriginTracking(CstOptionList optionlist) {
    { /* unamed block */{ /* unamed block */if ( (optionlist instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )optionlist) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )optionlist) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch325_end_4=(( tom.engine.adt.cst.types.CstOptionList )optionlist);do {{ /* unamed block */if (!( tomMatch325_end_4.isEmptyConcCstOption() )) {if ( ((( tom.engine.adt.cst.types.CstOption ) tomMatch325_end_4.getHeadConcCstOption() ) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {

 return  tomMatch325_end_4.getHeadConcCstOption() ; }}if ( tomMatch325_end_4.isEmptyConcCstOption() ) {tomMatch325_end_4=(( tom.engine.adt.cst.types.CstOptionList )optionlist);} else {tomMatch325_end_4= tomMatch325_end_4.getTailConcCstOption() ;}}} while(!( (tomMatch325_end_4==(( tom.engine.adt.cst.types.CstOptionList )optionlist)) ));}}}}

    throw new TomRuntimeException("info not found: " + optionlist);
  }

  private BQTerm makeBQVariableFromName(String name, String type, CstOptionList optionList) {
    //if more information is needed: concOption(OriginTracking(Name(name),getStartLine(optionList),getFileName(optionList))),
    Option info = makeOriginTracking(name,optionList);
    return  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(info, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make(name) , makeType(type)) ;
  }

  private Option makeOriginTracking(String name, CstOptionList optionList) {
    CstOption ot = getOriginTracking(optionList);
    return  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(name) , ot.getstartLine(), ot.getfileName()) ;
  }

  private TomType makeType(String type) {
    return  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , type,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
  }

  private HashMap<String,String> usedSlots = new HashMap<String,String>();
  private void putSlotType(String codomain, String slotName, String slotType) {
    String key = codomain+slotName;
    usedSlots.put(key,slotType);
  }

  private String getSlotType(String codomain, String slotName) {
    String key = codomain+slotName;
    return usedSlots.get(key);
  }

  private OptionList addDefaultModule(OptionList ol) {
    return tom_append_list_concOption(ol, tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.ModuleName.make("default") , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) );
  }

  private TomType getTomType(BQTerm bqt) {
    { /* unamed block */{ /* unamed block */if ( (bqt instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch326_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch326_4= null ; tom.engine.adt.tomtype.types.TomType  tomMatch326_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch326_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch326_5= true ;tomMatch326_3=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch326_1= tomMatch326_3.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch326_5= true ;tomMatch326_4=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch326_1= tomMatch326_4.getAstType() ;}}}if (tomMatch326_5) {

 
        return tomMatch326_1; 
      }}}{ /* unamed block */if ( (bqt instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch326_14= false ; tom.engine.adt.code.types.BQTerm  tomMatch326_9= null ; tom.engine.adt.code.types.BQTerm  tomMatch326_10= null ; tom.engine.adt.tomname.types.TomName  tomMatch326_7= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{ /* unamed block */tomMatch326_14= true ;tomMatch326_9=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch326_7= tomMatch326_9.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.bqterm.BQRecordAppl) ) {{ /* unamed block */tomMatch326_14= true ;tomMatch326_10=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch326_7= tomMatch326_10.getAstName() ;}}}if (tomMatch326_14) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch326_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {



        TomSymbol symbol = symbolTable.getSymbolFromName( tomMatch326_7.getString() );
        if(symbol!=null) {
          return symbol.getTypesToType().getCodomain();
        }
      }}}}}



    return symbolTable.TYPE_UNKNOWN;
  }

  private Theory extractTheory(CstSymbolList symbolList) {
    Theory res =  tom.engine.adt.theory.types.theory.EmptyconcElementaryTheory.make() ;
    { /* unamed block */{ /* unamed block */if ( (symbolList instanceof tom.engine.adt.cst.types.CstSymbolList) ) {if ( (((( tom.engine.adt.cst.types.CstSymbolList )symbolList) instanceof tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol) || ((( tom.engine.adt.cst.types.CstSymbolList )symbolList) instanceof tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol)) ) { tom.engine.adt.cst.types.CstSymbolList  tomMatch327_end_4=(( tom.engine.adt.cst.types.CstSymbolList )symbolList);do {{ /* unamed block */if (!( tomMatch327_end_4.isEmptyConcCstSymbol() )) { tom.engine.adt.cst.types.CstSymbol  tom___x= tomMatch327_end_4.getHeadConcCstSymbol() ;{ /* unamed block */{ /* unamed block */if ( (tom___x instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )tom___x) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_Symbol) ) {if ( ((( tom.engine.adt.cst.types.CstTheory ) (( tom.engine.adt.cst.types.CstSymbol )tom___x).gettheory() ) instanceof tom.engine.adt.cst.types.csttheory.Cst_TheoryAC) ) {




            res =  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( tom.engine.adt.theory.types.elementarytheory.AC.make() ,tom_append_list_concElementaryTheory(res, tom.engine.adt.theory.types.theory.EmptyconcElementaryTheory.make() )) ;
          }}}}{ /* unamed block */if ( (tom___x instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )tom___x) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_Symbol) ) {if ( ((( tom.engine.adt.cst.types.CstTheory ) (( tom.engine.adt.cst.types.CstSymbol )tom___x).gettheory() ) instanceof tom.engine.adt.cst.types.csttheory.Cst_TheoryAU) ) {


            res =  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( tom.engine.adt.theory.types.elementarytheory.AU.make() ,tom_append_list_concElementaryTheory(res, tom.engine.adt.theory.types.theory.EmptyconcElementaryTheory.make() )) ;
          }}}}}}if ( tomMatch327_end_4.isEmptyConcCstSymbol() ) {tomMatch327_end_4=(( tom.engine.adt.cst.types.CstSymbolList )symbolList);} else {tomMatch327_end_4= tomMatch327_end_4.getTailConcCstSymbol() ;}}} while(!( (tomMatch327_end_4==(( tom.engine.adt.cst.types.CstSymbolList )symbolList)) ));}}}}





    return res;
  }

}
