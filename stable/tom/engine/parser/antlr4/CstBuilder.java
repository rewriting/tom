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
import org.antlr.v4.runtime.misc.*;
//import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

//import tom.library.sl.*;

/*
 * CST builder
 * traverse the ANTLR tree and generate a Gom Cst_Program, of sort CstProgram
 */
public class CstBuilder extends TomIslandParserBaseListener {
        private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_AndConstraint() ) {       return l2;     } else if( l2.isEmptyCst_AndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {       if(  l1.getTailCst_AndConstraint() .isEmptyCst_AndConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,tom_append_list_Cst_AndConstraint( l1.getTailCst_AndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_AndConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getHeadCst_AndConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_AndConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getTailCst_AndConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_OrConstraint() ) {       return l2;     } else if( l2.isEmptyCst_OrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {       if(  l1.getTailCst_OrConstraint() .isEmptyCst_OrConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,tom_append_list_Cst_OrConstraint( l1.getTailCst_OrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_OrConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getHeadCst_OrConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_OrConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getTailCst_OrConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSlotList  tom_append_list_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList l1,  tom.engine.adt.cst.types.CstSlotList  l2) {     if( l1.isEmptyConcCstSlot() ) {       return l2;     } else if( l2.isEmptyConcCstSlot() ) {       return l1;     } else if(  l1.getTailConcCstSlot() .isEmptyConcCstSlot() ) {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,tom_append_list_ConcCstSlot( l1.getTailConcCstSlot() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSlotList  tom_get_slice_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList  begin,  tom.engine.adt.cst.types.CstSlotList  end, tom.engine.adt.cst.types.CstSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSlot()  ||  (end== tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( begin.getHeadConcCstSlot() ,( tom.engine.adt.cst.types.CstSlotList )tom_get_slice_ConcCstSlot( begin.getTailConcCstSlot() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBlockList  tom_append_list_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList l1,  tom.engine.adt.cst.types.CstBlockList  l2) {     if( l1.isEmptyConcCstBlock() ) {       return l2;     } else if( l2.isEmptyConcCstBlock() ) {       return l1;     } else if(  l1.getTailConcCstBlock() .isEmptyConcCstBlock() ) {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,tom_append_list_ConcCstBlock( l1.getTailConcCstBlock() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBlockList  tom_get_slice_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList  begin,  tom.engine.adt.cst.types.CstBlockList  end, tom.engine.adt.cst.types.CstBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBlock()  ||  (end== tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( begin.getHeadConcCstBlock() ,( tom.engine.adt.cst.types.CstBlockList )tom_get_slice_ConcCstBlock( begin.getTailConcCstBlock() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_append_list_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList l1,  tom.engine.adt.cst.types.CstPairSlotBQTermList  l2) {     if( l1.isEmptyConcCstPairSlotBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstPairSlotBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstPairSlotBQTerm() .isEmptyConcCstPairSlotBQTerm() ) {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,tom_append_list_ConcCstPairSlotBQTerm( l1.getTailConcCstPairSlotBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_get_slice_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList  begin,  tom.engine.adt.cst.types.CstPairSlotBQTermList  end, tom.engine.adt.cst.types.CstPairSlotBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairSlotBQTerm()  ||  (end== tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( begin.getHeadConcCstPairSlotBQTerm() ,( tom.engine.adt.cst.types.CstPairSlotBQTermList )tom_get_slice_ConcCstPairSlotBQTerm( begin.getTailConcCstPairSlotBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOptionList  tom_append_list_ConcCstOption( tom.engine.adt.cst.types.CstOptionList l1,  tom.engine.adt.cst.types.CstOptionList  l2) {     if( l1.isEmptyConcCstOption() ) {       return l2;     } else if( l2.isEmptyConcCstOption() ) {       return l1;     } else if(  l1.getTailConcCstOption() .isEmptyConcCstOption() ) {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,tom_append_list_ConcCstOption( l1.getTailConcCstOption() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOptionList  tom_get_slice_ConcCstOption( tom.engine.adt.cst.types.CstOptionList  begin,  tom.engine.adt.cst.types.CstOptionList  end, tom.engine.adt.cst.types.CstOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOption()  ||  (end== tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( begin.getHeadConcCstOption() ,( tom.engine.adt.cst.types.CstOptionList )tom_get_slice_ConcCstOption( begin.getTailConcCstOption() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPatternList  tom_append_list_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList l1,  tom.engine.adt.cst.types.CstPatternList  l2) {     if( l1.isEmptyConcCstPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPattern() ) {       return l1;     } else if(  l1.getTailConcCstPattern() .isEmptyConcCstPattern() ) {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,tom_append_list_ConcCstPattern( l1.getTailConcCstPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPatternList  tom_get_slice_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList  begin,  tom.engine.adt.cst.types.CstPatternList  end, tom.engine.adt.cst.types.CstPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPattern()  ||  (end== tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( begin.getHeadConcCstPattern() ,( tom.engine.adt.cst.types.CstPatternList )tom_get_slice_ConcCstPattern( begin.getTailConcCstPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstNameList  tom_append_list_ConcCstName( tom.engine.adt.cst.types.CstNameList l1,  tom.engine.adt.cst.types.CstNameList  l2) {     if( l1.isEmptyConcCstName() ) {       return l2;     } else if( l2.isEmptyConcCstName() ) {       return l1;     } else if(  l1.getTailConcCstName() .isEmptyConcCstName() ) {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,tom_append_list_ConcCstName( l1.getTailConcCstName() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstNameList  tom_get_slice_ConcCstName( tom.engine.adt.cst.types.CstNameList  begin,  tom.engine.adt.cst.types.CstNameList  end, tom.engine.adt.cst.types.CstNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstName()  ||  (end== tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( begin.getHeadConcCstName() ,( tom.engine.adt.cst.types.CstNameList )tom_get_slice_ConcCstName( begin.getTailConcCstName() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOperatorList  tom_append_list_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList l1,  tom.engine.adt.cst.types.CstOperatorList  l2) {     if( l1.isEmptyConcCstOperator() ) {       return l2;     } else if( l2.isEmptyConcCstOperator() ) {       return l1;     } else if(  l1.getTailConcCstOperator() .isEmptyConcCstOperator() ) {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,tom_append_list_ConcCstOperator( l1.getTailConcCstOperator() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOperatorList  tom_get_slice_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList  begin,  tom.engine.adt.cst.types.CstOperatorList  end, tom.engine.adt.cst.types.CstOperatorList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOperator()  ||  (end== tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( begin.getHeadConcCstOperator() ,( tom.engine.adt.cst.types.CstOperatorList )tom_get_slice_ConcCstOperator( begin.getTailConcCstOperator() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstTermList  tom_append_list_ConcCstTerm( tom.engine.adt.cst.types.CstTermList l1,  tom.engine.adt.cst.types.CstTermList  l2) {     if( l1.isEmptyConcCstTerm() ) {       return l2;     } else if( l2.isEmptyConcCstTerm() ) {       return l1;     } else if(  l1.getTailConcCstTerm() .isEmptyConcCstTerm() ) {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,tom_append_list_ConcCstTerm( l1.getTailConcCstTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstTermList  tom_get_slice_ConcCstTerm( tom.engine.adt.cst.types.CstTermList  begin,  tom.engine.adt.cst.types.CstTermList  end, tom.engine.adt.cst.types.CstTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstTerm()  ||  (end== tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( begin.getHeadConcCstTerm() ,( tom.engine.adt.cst.types.CstTermList )tom_get_slice_ConcCstTerm( begin.getTailConcCstTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBQTermList  tom_append_list_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList l1,  tom.engine.adt.cst.types.CstBQTermList  l2) {     if( l1.isEmptyConcCstBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstBQTerm() .isEmptyConcCstBQTerm() ) {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,tom_append_list_ConcCstBQTerm( l1.getTailConcCstBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBQTermList  tom_get_slice_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList  begin,  tom.engine.adt.cst.types.CstBQTermList  end, tom.engine.adt.cst.types.CstBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBQTerm()  ||  (end== tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( begin.getHeadConcCstBQTerm() ,( tom.engine.adt.cst.types.CstBQTermList )tom_get_slice_ConcCstBQTerm( begin.getTailConcCstBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_append_list_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList l1,  tom.engine.adt.cst.types.CstConstraintActionList  l2) {     if( l1.isEmptyConcCstConstraintAction() ) {       return l2;     } else if( l2.isEmptyConcCstConstraintAction() ) {       return l1;     } else if(  l1.getTailConcCstConstraintAction() .isEmptyConcCstConstraintAction() ) {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,tom_append_list_ConcCstConstraintAction( l1.getTailConcCstConstraintAction() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_get_slice_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList  begin,  tom.engine.adt.cst.types.CstConstraintActionList  end, tom.engine.adt.cst.types.CstConstraintActionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstConstraintAction()  ||  (end== tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( begin.getHeadConcCstConstraintAction() ,( tom.engine.adt.cst.types.CstConstraintActionList )tom_get_slice_ConcCstConstraintAction( begin.getTailConcCstConstraintAction() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstVisitList  tom_append_list_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList l1,  tom.engine.adt.cst.types.CstVisitList  l2) {     if( l1.isEmptyConcCstVisit() ) {       return l2;     } else if( l2.isEmptyConcCstVisit() ) {       return l1;     } else if(  l1.getTailConcCstVisit() .isEmptyConcCstVisit() ) {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,tom_append_list_ConcCstVisit( l1.getTailConcCstVisit() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstVisitList  tom_get_slice_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList  begin,  tom.engine.adt.cst.types.CstVisitList  end, tom.engine.adt.cst.types.CstVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstVisit()  ||  (end== tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( begin.getHeadConcCstVisit() ,( tom.engine.adt.cst.types.CstVisitList )tom_get_slice_ConcCstVisit( begin.getTailConcCstVisit() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairPatternList  tom_append_list_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList l1,  tom.engine.adt.cst.types.CstPairPatternList  l2) {     if( l1.isEmptyConcCstPairPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPairPattern() ) {       return l1;     } else if(  l1.getTailConcCstPairPattern() .isEmptyConcCstPairPattern() ) {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,tom_append_list_ConcCstPairPattern( l1.getTailConcCstPairPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairPatternList  tom_get_slice_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList  begin,  tom.engine.adt.cst.types.CstPairPatternList  end, tom.engine.adt.cst.types.CstPairPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairPattern()  ||  (end== tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( begin.getHeadConcCstPairPattern() ,( tom.engine.adt.cst.types.CstPairPatternList )tom_get_slice_ConcCstPairPattern( begin.getTailConcCstPairPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSymbolList  tom_append_list_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList l1,  tom.engine.adt.cst.types.CstSymbolList  l2) {     if( l1.isEmptyConcCstSymbol() ) {       return l2;     } else if( l2.isEmptyConcCstSymbol() ) {       return l1;     } else if(  l1.getTailConcCstSymbol() .isEmptyConcCstSymbol() ) {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,tom_append_list_ConcCstSymbol( l1.getTailConcCstSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSymbolList  tom_get_slice_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList  begin,  tom.engine.adt.cst.types.CstSymbolList  end, tom.engine.adt.cst.types.CstSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSymbol()  ||  (end== tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( begin.getHeadConcCstSymbol() ,( tom.engine.adt.cst.types.CstSymbolList )tom_get_slice_ConcCstSymbol( begin.getTailConcCstSymbol() ,end,tail)) ;   }    

  private String filename;
  private BufferedTokenStream tokens;
  private Set<Token> usedToken; // used to add spaces before of after a given token

  public CstBuilder(String filename, BufferedTokenStream tokens) {
    this.filename = filename;
    this.tokens = tokens;
    this.usedToken = new HashSet<Token>();
  }

  public void cleanUsedToken() {
    this.usedToken = new HashSet<Token>();
  }

  private static Logger logger = Logger.getLogger("tom.engine.typer.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private ParseTreeProperty<Object> values = new ParseTreeProperty<Object>();
  private void setValue(ParseTree node, Object value) { values.put(node, value); } 
  public Object getValue(ParseTree node) { return values.get(node); }
  public void setStringValue(ParseTree node, String value) { setValue(node, value); } 
  public String getStringValue(ParseTree node) { return (String) getValue(node); }
  public CstBlockList getBlockListFromBlock(ParseTree node) { return ((CstBlock) getValue(node)).getblocks(); }

  private ParseTreeProperty<Object> values2 = new ParseTreeProperty<Object>();
  private void setValue2(ParseTree node, Object value) { values2.put(node, value); } 
  public Object getValue2(ParseTree node) { return values2.get(node); }

  private void setValue(String debug, ParseTree node, Object value) { 
    values.put(node, value);
    //System.out.println(debug + ": " + value);
  } 


  /*
   * start : (island | water)*? ;
   */
  public void exitStart(TomIslandParser.StartContext ctx) {
    CstBlockList bl =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ;
    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);
      if(child instanceof TomIslandParser.IslandContext) {
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((CstBlock)getValue(child),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else if(child instanceof TomIslandParser.WaterContext) {
        //ParserRuleContext prc = (ParserRuleContext)child;
        //CstOption ot = extractOption(prc.getStart());
        //bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(buildHostblock((ParserRuleContext)child),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      }
    }
    setValue("exitStart",ctx,  tom.engine.adt.cst.types.cstprogram.Cst_Program.make(bl.reverse()) );
  }

  /*
   * island 
   *   : matchStatement
   *   | strategyStatement
   *   | includeStatement
   *   | gomStatement
   *   | typeterm
   *   | operator
   *   | oplist
   *   | oparray
   *   | bqcomposite
   *   | metaquote
   *   ;
   */
  public void exitIsland(TomIslandParser.IslandContext ctx) {
    ParseTree child = ctx.getChild(0);
    setValue("exitIsland",ctx,getValue(child));
  }

  /*
   * water
   *   : .
   *   ;
   */
  public void exitWater(TomIslandParser.WaterContext ctx) {
    setStringValue(ctx,ctx.getText());
  }

  /* 
   * metaquote
   *   : METAQUOTE
   *   ;
   */
  public void exitMetaquote(TomIslandParser.MetaquoteContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    String code = ctx.METAQUOTE().getText();
    CstBlockList bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make(optionList, code) , tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ;

    setValue("exitMetaquote", ctx, tom.engine.adt.cst.types.cstblock.Cst_Metaquote.make(optionList, bl) );
  }

  /*
   * matchStatement
   *   : MATCH (LPAREN (bqterm (COMMA bqterm)*)? RPAREN)? LBRACE actionRule* RBRACE 
   *   ;
   */
  public void exitMatchStatement(TomIslandParser.MatchStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBQTermList subjectList = buildCstBQTermList(ctx.bqterm());
    CstConstraintActionList constraintActionList = buildCstConstraintActionList(ctx.actionRule());
    CstBlock res =  tom.engine.adt.cst.types.cstblock.Cst_MatchConstruct.make(optionList, subjectList, constraintActionList) ;
    setValue("exitMatchStatement", ctx,res);
  }

  /*
   * strategyStatement
   *   : STRATEGY ID LPAREN slotList? RPAREN EXTENDS bqterm LBRACE visit* RBRACE
   *   ;
   */
  public void exitStrategyStatement(TomIslandParser.StrategyStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstName name =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) ;
    CstSlotList argumentList =  tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ;
    // if there are arguments
    if(ctx.slotList() != null) {
      argumentList = (CstSlotList) getValue(ctx.slotList());
    }
    CstVisitList visitList = buildCstVisitList(ctx.visit());

    CstBlock res =  tom.engine.adt.cst.types.cstblock.Cst_StrategyConstruct.make(optionList, name, argumentList, (CstBQTerm)getValue(ctx.bqterm()), visitList) ;
    setValue("exitStrategy", ctx,res);
  }

  /*
   * includeStatement
   *   : INCLUDE LBRACE ID ((SLASH|BACKSLASH) ID)*  RBRACE 
   *   ;
   */
  public void exitIncludeStatement(TomIslandParser.IncludeStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    String filename = "";
    for(int i = 2 ; i<ctx.getChildCount()-1 ; i++) {
      // skip %include {, and the last }
      ParseTree child = ctx.getChild(i);
      filename += child.getText();
    }
    setValue("exitIncludeStatement", ctx, tom.engine.adt.cst.types.cstblock.Cst_IncludeFile.make(optionList, filename) );
  }

  /*
   * gomStatement
   *   : GOM gomOptions? block
   *   ;
   */
  public void exitGomStatement(TomIslandParser.GomStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBlock block = (CstBlock) getValue(ctx.block());
    String text = getText(block.getoptionList());
    // remove starting '{' and ending '}'
    text = text.substring(1,text.length()-1).trim();
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    if(ctx.gomOptions() != null) {
      nameList = (CstNameList) getValue(ctx.gomOptions());
    }

    setValue("exitGomStatement", ctx, tom.engine.adt.cst.types.cstblock.Cst_GomConstruct.make(optionList, nameList, text) );
  }

  /*
   * gomOptions
   *   : LPAREN DMINUSID (COMMA DMINUSID)* RPAREN
   *   ;
   */
  public void exitGomOptions(TomIslandParser.GomOptionsContext ctx) {
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    for(TerminalNode e:ctx.DMINUSID()) {
      nameList = tom_append_list_ConcCstName(nameList, tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(e.getText()) , tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) );
    }
    setValue("exitGomOptions", ctx, nameList);
  }

  /*
   * visit
   *   : VISIT ID LBRACE actionRule* RBRACE
   *   ;
   */
  public void exitVisit(TomIslandParser.VisitContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstConstraintActionList l = buildCstConstraintActionList(ctx.actionRule());
    CstVisit res =  tom.engine.adt.cst.types.cstvisit.Cst_VisitTerm.make( tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.ID().getText()) , l, optionList) ;
    setValue("exitVisit", ctx,res);
  }

  /*
   * actionRule
   *   : patternlist ((AND | OR) constraint)? ARROW block
   *   | patternlist ((AND | OR) constraint)? ARROW bqterm
   *   | c=constraint ARROW block
   *   | c=constraint ARROW bqterm
   *   ;
   */
  public void exitActionRule(TomIslandParser.ActionRuleContext ctx) {
    CstConstraintAction res = null;
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBlockList action = null;
    if(ctx.block() != null) {
      action = getBlockListFromBlock(ctx.block());
    } else if(ctx.bqterm() != null) {
      action =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( tom.engine.adt.cst.types.cstblock.Cst_ReturnBQTerm.make((CstBQTerm)getValue(ctx.bqterm())) , tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ;
    }
    CstConstraint constraint =  tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ;
    if(ctx.c != null) {
      constraint = (CstConstraint)getValue(ctx.c);
    } else {
      for(CstPattern p:((CstPatternList)getValue(ctx.patternlist())).getCollectionConcCstPattern()) {
        constraint =  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(constraint, tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( tom.engine.adt.cst.types.cstconstraint.Cst_MatchArgumentConstraint.make(p) , tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) ) ;
      }
      if(ctx.AND() != null) {
        constraint =  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(constraint, tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make((CstConstraint)getValue(ctx.constraint()), tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) ) ;
      } else if(ctx.OR() != null) {
        constraint =  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(constraint, tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make((CstConstraint)getValue(ctx.constraint()), tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) ) ;
      }
    }

    res =  tom.engine.adt.cst.types.cstconstraintaction.Cst_ConstraintAction.make(constraint, action, optionList) ;
    setValue("exitActionRule", ctx,res);
  }

  /*
   * block 
   *   : LBRACE (island | block | water)*? RBRACE
   *   ;
   */
  public void exitBlock(TomIslandParser.BlockContext ctx) {
    CstBlockList bl =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ;

    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);

      if(child instanceof TomIslandParser.IslandContext) {
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((CstBlock)getValue(child),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else if(child instanceof TomIslandParser.BlockContext) {
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((CstBlock)getValue(child),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else if(child instanceof TomIslandParser.WaterContext) {
        //ParserRuleContext prc = (ParserRuleContext)child;
        //CstOption ot = extractOption(prc.getStart());
        //bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(buildHostblock((ParserRuleContext)child),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      }
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx, tom.engine.adt.cst.types.cstblock.Cst_UnamedBlock.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(otext, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , bl.reverse()) );
  }

  /*
   * slotList
   *   : slot (COMMA slot)*
   *   ;
   */
  public void exitSlotList(TomIslandParser.SlotListContext ctx) {
    CstSlotList res = buildCstSlotList(ctx.slot());
    setValue("exitSlotList", ctx,res);
  }

  /*
   * slot
   *   : id1=ID COLON? id2=ID
   *   ;
   */
  public void exitSlot(TomIslandParser.SlotContext ctx) {
    CstSlot res = null;
    if(ctx.COLON() != null) {
      res =  tom.engine.adt.cst.types.cstslot.Cst_Slot.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.id2.getText()) ) ;
    } else {
      res =  tom.engine.adt.cst.types.cstslot.Cst_Slot.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) ,  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.id1.getText()) ) ;
    }
    setValue("exitSlot",ctx,res);
  }

  /*
   * patternlist
   *   : pattern (COMMA pattern)* 
   *   ;
   */
  public void exitPatternlist(TomIslandParser.PatternlistContext ctx) {
    CstPatternList res = buildCstPatternList(ctx.pattern());
    setValue("exitPatternList", ctx,res);
  }

  /*
   * constraint
   *   : constraint AND constraint
   *   | constraint OR constraint
   *   | pattern MATCH_SYMBOL bqterm
   *   | term GREATERTHAN term
   *   | term GREATEROREQ term
   *   | term LOWERTHAN term
   *   | term LOWEROREQ term
   *   | term DOUBLEEQ term
   *   | term DIFFERENT term
   *   | LPAREN c=constraint RPAREN
   *   ;
   */
  public void exitConstraint(TomIslandParser.ConstraintContext ctx) {
    CstConstraint res = null;
    if(ctx.AND() != null || ctx.OR() != null) {
      CstConstraint lhs = (CstConstraint)getValue(ctx.constraint(0));
      CstConstraint rhs = (CstConstraint)getValue(ctx.constraint(1));
      res = (ctx.AND() != null)? tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(lhs, tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(rhs, tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) ) : tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(lhs, tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(rhs, tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) ) ;
    } else if(ctx.MATCH_SYMBOL() != null) {
      CstPattern lhs = (CstPattern)getValue(ctx.pattern());
      CstBQTerm rhs = (CstBQTerm)getValue(ctx.bqterm());
      CstType rhs_type = (CstType)getValue2(ctx.bqterm());
      res =  tom.engine.adt.cst.types.cstconstraint.Cst_MatchTermConstraint.make(lhs, rhs, rhs_type) ;
    } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
      res = (CstConstraint)getValue(ctx.c);
    } else {
      CstTerm lhs = (CstTerm)getValue(ctx.term(0));
      CstTerm rhs = (CstTerm)getValue(ctx.term(1));
      if(ctx.GREATERTHAN() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumGreaterThan.make(lhs, rhs) ; }
      else if(ctx.GREATEROREQ() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumGreaterOrEqualThan.make(lhs, rhs) ; }
      else if(ctx.LOWERTHAN() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumLessThan.make(lhs, rhs) ; }
      else if(ctx.LOWEROREQ() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumLessOrEqualThan.make(lhs, rhs) ; }
      else if(ctx.DOUBLEEQ() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_EqualTo.make(lhs, rhs) ; }
      else if(ctx.DIFFERENT() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_Different.make(lhs, rhs) ; }
    }

    setValue("exitConstraint",ctx,res);
  }

  /*
   * term
   *   : var=ID STAR?
   *   | fsym=ID LPAREN (term (COMMA term)*)? RPAREN 
   *   | constant
   *   ;
   */
  public void exitTerm(TomIslandParser.TermContext ctx) {
    CstTerm res = null;
    if(ctx.var != null && ctx.STAR() == null) {
      res =  tom.engine.adt.cst.types.cstterm.Cst_TermVariable.make(ctx.var.getText()) ;
    } else if(ctx.var != null && ctx.STAR() != null) {
      res =  tom.engine.adt.cst.types.cstterm.Cst_TermVariableStar.make(ctx.var.getText()) ;
    } else if(ctx.fsym != null) {
      CstTermList args = buildCstTermList(ctx.term());
      res =  tom.engine.adt.cst.types.cstterm.Cst_TermAppl.make(ctx.fsym.getText(), args) ;
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res =  tom.engine.adt.cst.types.cstterm.Cst_TermConstant.make(cst.getvalue()) ;
    } 
    setValue("exitTerm",ctx,res);
  }

  /*
   * bqterm
   *   : codomain=ID? BQUOTE? fsym=ID LPAREN (bqterm (COMMA bqterm)*)? RPAREN 
   *   | codomain=ID? BQUOTE? var=ID STAR?
   *   | codomain=ID? constant
   *   ;
   */
  public void exitBqterm(TomIslandParser.BqtermContext ctx) {
    CstBQTerm res = null;
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType type = (ctx.codomain != null)? tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) : tom.engine.adt.cst.types.csttype.Cst_TypeUnknown.make() ;

    if(ctx.fsym != null && ctx.LPAREN() != null) {
      CstBQTermList args = buildCstBQTermList(ctx.bqterm());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQAppl.make(optionList, ctx.fsym.getText(), args) ;
    } else if(ctx.fsym != null && ctx.LSQUAREBR() != null) {
      CstPairSlotBQTermList args = buildCstPairSlotBQTermList(ctx.pairSlotBqterm());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQRecordAppl.make(optionList, ctx.fsym.getText(), args) ;
    } else if(ctx.var != null && ctx.STAR() != null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQVarStar.make(optionList, ctx.var.getText(), type) ;
    } else if(ctx.var != null && ctx.STAR() == null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQVar.make(optionList, ctx.var.getText(), type) ;
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQConstant.make(optionList, cst.getvalue()) ;
    } else if(ctx.UNDERSCORE() != null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQUnderscore.make() ;
    }

    setValue2(ctx,type);
    setValue("exitBqterm",ctx,res);
  }

  /*
   * pairSlotBqterm
   *   : ID EQUAL bqterm
   *   ;
   */
  public void exitPairSlotBqterm(TomIslandParser.PairSlotBqtermContext ctx) {
    CstPairSlotBQTerm res = null;
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstName slotName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) ;
    CstBQTerm bqterm = (CstBQTerm) getValue(ctx.bqterm());
    res =  tom.engine.adt.cst.types.cstpairslotbqterm.Cst_PairSlotBQTerm.make(optionList, slotName, bqterm) ;
    setValue("exitPairSlotBqterm",ctx,res);
  }

  /*
   * bqcomposite
   *   : BQUOTE composite
   *   | BQUOTE fsym=ID LSQUAREBR (pairSlotBqterm (COMMA pairSlotBqterm)*)? RSQUAREBR 
   *   ;
   */
  public void exitBqcomposite(TomIslandParser.BqcompositeContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBlock res = null;
    if(ctx.fsym != null && ctx.LSQUAREBR() != null) {
      CstPairSlotBQTermList args = buildCstPairSlotBQTermList(ctx.pairSlotBqterm());
      res =  tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock.make( tom.engine.adt.cst.types.cstbqterm.Cst_BQRecordAppl.make(optionList, ctx.fsym.getText(), args) ) ;
    } else {
      res =  tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock.make((CstBQTerm)getValue(ctx.composite())) ;
    }
    setValue("exitBqcomposite",ctx,res);
  }

  /*
   * composite
   *   : fsym=ID LPAREN composite*? RPAREN
   *   | LPAREN composite*? RPAREN
   *   | var=ID STAR?
   *   | constant
   *   | UNDERSCORE
   *   | water
   *   ;
   */
  public void exitComposite(TomIslandParser.CompositeContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBQTerm res = null;
    CstType type =  tom.engine.adt.cst.types.csttype.Cst_TypeUnknown.make() ;

    if(ctx.fsym != null) {
      CstBQTermList args =  tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ;

      CstBQTermList accu =  tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ;
      for(ParserRuleContext e:ctx.composite()) {
        CstBQTerm bq = (CstBQTerm)getValue(e);
        if(bq.isCst_ITL() && bq.getcode() == ",") {
          // put all elements of accu as a subterm
          CstBQTerm newComposite =  tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite.make( tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() , accu) ;
          //CstBQTerm newComposite = flattenComposite(`Cst_BQComposite(ConcCstOption(),accu));
          //newComposite = mergeITL(newComposite);
          args = tom_append_list_ConcCstBQTerm(args, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make(newComposite, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
          accu =  tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ;
        } else {
          // retrieve elements separated by COMMA
          accu = tom_append_list_ConcCstBQTerm(accu, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make(bq, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
        }
      }
      // flush the last accu
      { /* unamed block */{ /* unamed block */if ( (accu instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )accu) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )accu) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) {if (!( (( tom.engine.adt.cst.types.CstBQTermList )accu).isEmptyConcCstBQTerm() )) {if (  (( tom.engine.adt.cst.types.CstBQTermList )accu).getTailConcCstBQTerm() .isEmptyConcCstBQTerm() ) {

          // single element
          args = tom_append_list_ConcCstBQTerm(args, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( (( tom.engine.adt.cst.types.CstBQTermList )accu).getHeadConcCstBQTerm() , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
        }}}}}{ /* unamed block */if ( (accu instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )accu) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )accu) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) {if (!( (( tom.engine.adt.cst.types.CstBQTermList )accu).isEmptyConcCstBQTerm() )) {if (!(  (( tom.engine.adt.cst.types.CstBQTermList )accu).getTailConcCstBQTerm() .isEmptyConcCstBQTerm() )) {


          // multiple elements: build a composite
          //CstBQTerm newComposite = flattenComposite(`Cst_BQComposite(ConcCstOption(),accu));
          CstBQTerm newComposite =  tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite.make( tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() , accu) ;
          //newComposite = mergeITL(newComposite);
          args = tom_append_list_ConcCstBQTerm(args, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make(newComposite, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
        }}}}}}


      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQAppl.make(optionList, ctx.fsym.getText(), args) ;
    } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
      CstOptionList optionList1 =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.LPAREN().getSymbol()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
      CstOptionList optionList2 =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.RPAREN().getSymbol()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
      CstBQTermList args = buildCstBQTermList(ctx.composite());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite.make(optionList,  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(optionList1, ctx.LPAREN().getText()) ,tom_append_list_ConcCstBQTerm(args, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(optionList2, ctx.RPAREN().getText()) 
            , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) )) ) 



;

      //res = mergeITL(res);
    } else if(ctx.var != null && ctx.STAR() == null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQVar.make(optionList, ctx.var.getText(), type) ;
    } else if(ctx.var != null && ctx.STAR() != null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQVarStar.make(optionList, ctx.var.getText(), type) ;
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQConstant.make(optionList, cst.getvalue()) ;
    } else if(ctx.UNDERSCORE() != null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQUnderscore.make() ;
    } else if (ctx.water() != null) {
      //System.out.println("composite water");
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(optionList, getStringValue(ctx.water())) ;
    }

    setValue("exitComposite",ctx,res);
  }

  /*
   * pattern
   *   : ID AT pattern 
   *   | ANTI pattern
   *   | fsymbol explicitArgs
   *   | fsymbol implicitArgs
   *   | var=ID STAR?
   *   | UNDERSCORE STAR?
   *   | constant STAR?
   *   ;
   */
  public void exitPattern(TomIslandParser.PatternContext ctx) {
    CstPattern res = null;
    if(ctx.AT() != null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_AnnotatedPattern.make((CstPattern)getValue(ctx.pattern()), ctx.ID().getText()) ;
    } else if(ctx.ANTI() != null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_Anti.make((CstPattern)getValue(ctx.pattern())) ;
    } else if(ctx.explicitArgs() != null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_Appl.make((CstSymbolList)getValue(ctx.fsymbol()), (CstPatternList)getValue(ctx.explicitArgs())) ;
    } else if(ctx.implicitArgs() != null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_RecordAppl.make((CstSymbolList)getValue(ctx.fsymbol()), (CstPairPatternList)getValue(ctx.implicitArgs())) ;
    } else if(ctx.var != null && ctx.STAR() == null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_Variable.make(ctx.var.getText()) ;
    } else if(ctx.var != null && ctx.STAR() != null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_VariableStar.make(ctx.var.getText()) ;
    } else if(ctx.UNDERSCORE() != null && ctx.STAR() == null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_UnamedVariable.make() ;
    } else if(ctx.UNDERSCORE() != null && ctx.STAR() != null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_UnamedVariableStar.make() ;
    } else if(ctx.constant() != null) {
      CstSymbolList symbolList = buildCstSymbolList(ctx.constant());
      res =  tom.engine.adt.cst.types.cstpattern.Cst_ConstantOr.make(symbolList) ;
      //CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      //res = `Cst_Constant(cst);
    }
    /*
    } else if(ctx.constant() != null && ctx.STAR() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res = `Cst_ConstantStar(cst);
    }
    */
    setValue("exitPattern",ctx,res);
  }

  /*
   * fsymbol 
   *   : headSymbol
   *   | LPAREN headSymbol (PIPE headSymbol)* RPAREN
   *   ;
   */
  public void exitFsymbol(TomIslandParser.FsymbolContext ctx) {
    CstSymbolList res = buildCstSymbolList(ctx.headSymbol());
    setValue("exitFsymbol",ctx,res);
  }

  /*
   * headSymbol
   *   : ID QMARK?
   *   | ID DQMARK?
   *   | constant
   *   ;
   */
  public void exitHeadSymbol(TomIslandParser.HeadSymbolContext ctx) {
    CstSymbol res = null;
    if(ctx.QMARK() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_Symbol.make(ctx.ID().getText(),  tom.engine.adt.cst.types.csttheory.Cst_TheoryAU.make() ) ;
    } else if(ctx.DQMARK() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_Symbol.make(ctx.ID().getText(),  tom.engine.adt.cst.types.csttheory.Cst_TheoryAC.make() ) ;
    } else if(ctx.ID() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_Symbol.make(ctx.ID().getText(),  tom.engine.adt.cst.types.csttheory.Cst_TheoryDEFAULT.make() ) ;
    } else if(ctx.constant() != null) {
      res = (CstSymbol) getValue(ctx.constant());
    } 
    setValue("exitHeadSymbol",ctx,res);
  }

  /*
   * constant
   *   : INTEGER
   *   | LONG
   *   | CHAR
   *   | DOUBLE
   *   | STRING
   *   ;
   */
  public void exitConstant(TomIslandParser.ConstantContext ctx) {
    CstSymbol res = null;
    if(ctx.INTEGER() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolInt.make(ctx.INTEGER().getText()) ;
    } else if(ctx.LONG() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolLong.make(ctx.LONG().getText()) ;
    } else if(ctx.CHAR() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolChar.make(ctx.CHAR().getText()) ;
    } else if(ctx.DOUBLE() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolDouble.make(ctx.DOUBLE().getText()) ;
    } else if(ctx.STRING() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolString.make(ctx.STRING().getText()) ;
    }
    setValue("exitConstant",ctx,res);
  }

  /*
   * explicitArgs
   *   : LPAREN (pattern (COMMA pattern)*)? RPAREN
   *   ;
   */
  public void exitExplicitArgs(TomIslandParser.ExplicitArgsContext ctx) {
    int n = ctx.pattern().size();
    CstPatternList res =  tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ;
    for(int i=0 ; i<n ; i++) {
      res = tom_append_list_ConcCstPattern(res, tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make((CstPattern)getValue(ctx.pattern(i)), tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) );
    }
    setValue("exitExplicitArgs",ctx,res);
  }

  /*
   * implicitArgs
   *   : LSQUAREBR (ID EQUAL pattern (COMMA ID EQUAL pattern)*)? RSQUAREBR 
   *   ;
   */
  public void exitImplicitArgs(TomIslandParser.ImplicitArgsContext ctx) {
    int n = ctx.ID().size();
    CstPairPatternList res =  tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ;
    for(int i=0 ; i<n ; i++) {
      res = tom_append_list_ConcCstPairPattern(res, tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( tom.engine.adt.cst.types.cstpairpattern.Cst_PairPattern.make(ctx.ID(i).getText(), (CstPattern)getValue(ctx.pattern(i))) , tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ) );
    }
    setValue("exitImplicitArgs",ctx,res);
  }

  /*
   * typeterm
   *   : TYPETERM type=ID (EXTENDS supertype=ID)? LBRACE 
   *     implement isSort? equalsTerm?
   *     RBRACE
   *   ;
   */
  public void exitTypeterm(TomIslandParser.TypetermContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType typeName =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.type.getText()) ;
    CstType extendsTypeName =  tom.engine.adt.cst.types.csttype.Cst_TypeUnknown.make() ;
    if(ctx.supertype != null) {
      extendsTypeName =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.supertype.getText()) ;
    }
    CstOperatorList operatorList =  tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ;
    operatorList = addCstOperator(operatorList, ctx.implement());
    operatorList = addCstOperator(operatorList, ctx.isSort());
    operatorList = addCstOperator(operatorList, ctx.equalsTerm());
    setValue("exitTypeterm", ctx,
         tom.engine.adt.cst.types.cstblock.Cst_TypetermConstruct.make(optionList, typeName, extendsTypeName, operatorList) );
  }

  /*
   * operator
   *   : OP codomain=ID opname=ID LPAREN slotList? RPAREN LBRACE 
   *     (isFsym | make | getSlot | getDefault)*
   *     RBRACE
   *   ;
   */
  public void exitOperator(TomIslandParser.OperatorContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType codomain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) ;
    CstName ctorName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.opname.getText()) ;
    // fill arguments
    CstSlotList argumentList =  tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ;
    if(ctx.slotList() != null) {
      argumentList = (CstSlotList) getValue(ctx.slotList());
    }
    // fill constructors
    CstOperatorList operatorList =  tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ;
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.make());
    operatorList = addCstOperator(operatorList, ctx.getSlot());
    operatorList = addCstOperator(operatorList, ctx.getDefault());
    setValue("exitOperator", ctx,
         tom.engine.adt.cst.types.cstblock.Cst_OpConstruct.make(optionList, codomain, ctorName, argumentList, operatorList) );
  }

  /*
   * oplist
   *   : OPLIST codomain=ID opname=ID LPAREN domain=ID STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyList | makeInsertList | getHead | getTail | isEmptyList)*
   *     RBRACE
   *   ;
   */
  public void exitOplist(TomIslandParser.OplistContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType codomain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) ;
    CstName ctorName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.opname.getText()) ;
    CstType domain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.domain.getText()) ;
    // fill constructors
    CstOperatorList operatorList =  tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ;
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.makeEmptyList());
    operatorList = addCstOperator(operatorList, ctx.makeInsertList());
    operatorList = addCstOperator(operatorList, ctx.getHead());
    operatorList = addCstOperator(operatorList, ctx.getTail());
    operatorList = addCstOperator(operatorList, ctx.isEmptyList());
    setValue("exitOpList", ctx,
         tom.engine.adt.cst.types.cstblock.Cst_OpListConstruct.make(optionList, codomain, ctorName, domain, operatorList) );
  }

  /*
   * oparray
   *   : OPARRAY codomain=ID opname=ID LPAREN domain=ID STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyArray | makeAppendArray | getElement | getSize)*
   *     RBRACE
   *   ;
   */
  public void exitOparray(TomIslandParser.OparrayContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType codomain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) ;
    CstName ctorName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.opname.getText()) ;
    CstType domain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.domain.getText()) ;
    // fill constructors
    CstOperatorList operatorList =  tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ;
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.makeEmptyArray());
    operatorList = addCstOperator(operatorList, ctx.makeAppendArray());
    operatorList = addCstOperator(operatorList, ctx.getElement());
    operatorList = addCstOperator(operatorList, ctx.getSize());
    setValue("exitOpArray", ctx,
         tom.engine.adt.cst.types.cstblock.Cst_OpArrayConstruct.make(optionList, codomain, ctorName, domain, operatorList) );
  }

  /*
   * implement
   *   : IMPLEMENT block
   *   ;
   */
  public void exitImplement(TomIslandParser.ImplementContext ctx) {
    setValue("exitImplement", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Implement.make(getBlockListFromBlock(ctx.block())) );
  }

  /*
   * equalsTerm
   *   : EQUALS LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitEqualsTerm(TomIslandParser.EqualsTermContext ctx) {
    setValue("exitEquals", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Equals.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * isSort
   *   : IS_SORT LPAREN ID RPAREN block
   *   ;
   */
  public void exitIsSort(TomIslandParser.IsSortContext ctx) {
    setValue("exitIsSort", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsSort.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * isFsym
   *   : IS_FSYM LPAREN ID RPAREN block
   *   ;
   */
  public void exitIsFsym(TomIslandParser.IsFsymContext ctx) {
    setValue("exitIsFsym", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsFsym.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * make
   *   : MAKE LPAREN (ID (COMMA ID)*)? RPAREN block
   *   ;
   */
  public void exitMake(TomIslandParser.MakeContext ctx) {
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    for(TerminalNode e:ctx.ID()) {
      nameList = tom_append_list_ConcCstName(nameList, tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(e.getText()) , tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) );
    }
    setValue("exitMake", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Make.make(nameList, getBlockListFromBlock(ctx.block())) );
  }

  /*
   * makeEmptyList
   *   : MAKE_EMPTY LPAREN RPAREN block
   *   ;
   */
  public void exitMakeEmptyList(TomIslandParser.MakeEmptyListContext ctx) {
    setValue("exitMakeEmptyList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyList.make(getBlockListFromBlock(ctx.block())) );
  }

  /*
   * makeEmptyArray
   *   : MAKE_EMPTY LPAREN ID RPAREN block
   *   ;
   */
  public void exitMakeEmptyArray(TomIslandParser.MakeEmptyArrayContext ctx) {
    setValue("exitMakeEmptyArray", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyArray.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * makeAppendArray
   *   : MAKE_APPEND LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitMakeAppendArray(TomIslandParser.MakeAppendArrayContext ctx) {
    setValue("exitMakeAppendArray", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeAppend.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * makeInsertList
   *   : MAKE_INSERT LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitMakeInsertList(TomIslandParser.MakeInsertListContext ctx) {
    setValue("exitMakeInsertList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeInsert.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * getSlot
   *   : GET_SLOT LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitGetSlot(TomIslandParser.GetSlotContext ctx) {
    setValue("exitGetSlot", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetSlot.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * getHead
   *   : GET_HEAD LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetHead(TomIslandParser.GetHeadContext ctx) {
    setValue("exitGetHead", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetHead.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * getTail
   *   : GET_TAIL LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetTail(TomIslandParser.GetTailContext ctx) {
    setValue("exitGetTail", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetTail.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * getElement
   *   : GET_ELEMENT LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitGetElement(TomIslandParser.GetElementContext ctx) {
    setValue("exitGetElement", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetElement.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * isEmptyList
   *   : IS_EMPTY LPAREN ID RPAREN block
   *   ;
   */
  public void exitIsEmptyList(TomIslandParser.IsEmptyListContext ctx) {
    setValue("exitIsEmptyList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsEmpty.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * getSize
   *   : GET_SIZE LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetSize(TomIslandParser.GetSizeContext ctx) {
    setValue("exitGetSize", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetSize.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * getDefault
   *   : GET_DEFAULT LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetDefault(TomIslandParser.GetDefaultContext ctx) {
    setValue("exitGetDefault", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetDefault.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.ID().getText()) , getBlockListFromBlock(ctx.block())) );
  }

  /*
   * End of grammar
   */

  private String getText(CstOptionList ol) {
    { /* unamed block */{ /* unamed block */if ( (ol instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )ol) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )ol) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch330_end_4=(( tom.engine.adt.cst.types.CstOptionList )ol);do {{ /* unamed block */if (!( tomMatch330_end_4.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch330_8= tomMatch330_end_4.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch330_8) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginText) ) {

        return  tomMatch330_8.gettext() ;
      }}if ( tomMatch330_end_4.isEmptyConcCstOption() ) {tomMatch330_end_4=(( tom.engine.adt.cst.types.CstOptionList )ol);} else {tomMatch330_end_4= tomMatch330_end_4.getTailConcCstOption() ;}}} while(!( (tomMatch330_end_4==(( tom.engine.adt.cst.types.CstOptionList )ol)) ));}}}}

    return "";
  }

  /*
   * given a context corresponding to water token
   * search spaces and layouts in hidden channels to
   * build a HOSTBLOCK with spaces and layout
   */
  private CstBlock buildHostblock(ParserRuleContext ctx) {
    String s = getStringValue(ctx);

    Token token = ctx.getStart();
    int firstCharLine = token.getLine();
    int firstCharColumn = token.getCharPositionInLine()+1;
    int lastCharLine = firstCharLine;
    int lastCharColumn = firstCharColumn + token.getText().length();

    List<Token> left = tokens.getHiddenTokensToLeft(token.getTokenIndex());
    List<Token> right = tokens.getHiddenTokensToRight(token.getTokenIndex());

    String sleft = "";
    String sright = "";
    if(left != null) {
      Token firstToken = left.get(0);
      if(!usedToken.contains(firstToken)) {
        // a given token should be used only once
        firstCharLine = firstToken.getLine();
        firstCharColumn = firstToken.getCharPositionInLine()+1;
      }

      for(Token space:left) {
        if(!usedToken.contains(space)) {
          sleft += space.getText();
          usedToken.add(space);
        }
      }
    }

    if(right != null) {
      String newline = System.getProperty("line.separator");
      Token lastToken = right.get(right.size()-1);
      //System.out.println("lastToken: " + lastToken);
      if(!usedToken.contains(lastToken)) {
        if(lastToken.getText().equals(newline)) {
          lastCharColumn = 1;
          lastCharLine = firstCharLine + 1;
        } else {
          lastCharLine = lastToken.getLine();
          lastCharColumn = lastToken.getCharPositionInLine()+1 + lastToken.getText().length();
        }
      }

      for(Token space:right) {
        if(!usedToken.contains(space)) {
          sright += space.getText();
          usedToken.add(space);
        }
      }

    }
    s = sleft + s + sright;


    String source = token.getTokenSource().getSourceName();
    if(source == IntStream.UNKNOWN_SOURCE_NAME) {
      source = this.filename;
    }
    CstOption ot =  tom.engine.adt.cst.types.cstoption.Cst_OriginTracking.make(source, firstCharLine, firstCharColumn, lastCharLine, lastCharColumn) ;  
    //System.out.println("ot: " + ot);
    //System.out.println("hb: " + `HOSTBLOCK(ConcCstOption(ot), s));
    return  tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(ot, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , s) ;
  }

  /*
   * given a context
   * returns the string corresponding to the parsed source code
   * (including spaces and layout)
   */
  private CstOption extractText(ParserRuleContext ctx) {
    int a = ctx.start.getStartIndex();
    int b = ctx.stop.getStopIndex();
    Interval interval = new Interval(a,b);
    //System.out.println("interval1: " + interval1);
    //System.out.println("interval2: " + interval2);
    String text = ctx.getStart().getInputStream().getText(interval);
    //System.out.println("text: " + text);
    return  tom.engine.adt.cst.types.cstoption.Cst_OriginText.make(text) ;
  }

  private CstOption extractOption(Token t) {
    String newline = System.getProperty("line.separator");
    String text = t.getText();
    //System.out.println("text: '" + text + "'");

    int firstCharLine = t.getLine();
    int firstCharColumn = t.getCharPositionInLine()+1;
    int lastCharLine;
    int lastCharColumn;

    if(text.equals(newline)) {
      lastCharColumn = 1;
      lastCharLine = firstCharLine + 1;
    } else {
      lastCharColumn = firstCharColumn + text.length();
      lastCharLine = firstCharLine;
    }

    //System.out.println(%[extractOption: '@text@' (@firstCharLine@,@firstCharColumn@) -- (@lastCharLine@,@lastCharColumn@)]%);

    String source = t.getTokenSource().getSourceName();
    if(source == IntStream.UNKNOWN_SOURCE_NAME) {
      source = this.filename;
    }
    return  tom.engine.adt.cst.types.cstoption.Cst_OriginTracking.make(source, firstCharLine, firstCharColumn, lastCharLine, lastCharColumn) ;  
  }


  public CstOperatorList addCstOperator(CstOperatorList operatorList, ParserRuleContext ctx) {
    if(ctx != null) {
      operatorList = tom_append_list_ConcCstOperator(operatorList, tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make((CstOperator)getValue(ctx), tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ) );
    }
    return operatorList;
  }

  public CstOperatorList addCstOperator(CstOperatorList operatorList, List<? extends ParserRuleContext> ctxList) {
    for(ParserRuleContext e:ctxList) {
      operatorList = addCstOperator(operatorList, e);
    }
    return operatorList;
  }

  /*
   * convert list of context into CstList 
   */
  private CstBQTermList buildCstBQTermList(List<? extends ParserRuleContext> ctx) {
    CstBQTermList res =  tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstBQTerm(res, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make((CstBQTerm)getValue(e), tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
      }
    }
    return res;
  }
  
  private CstPairSlotBQTermList buildCstPairSlotBQTermList(List<? extends ParserRuleContext> ctx) {
    CstPairSlotBQTermList res =  tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstPairSlotBQTerm(res, tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make((CstPairSlotBQTerm)getValue(e), tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ) );
      }
    }
    return res;
  }

  private CstConstraintActionList buildCstConstraintActionList(List<? extends ParserRuleContext> ctx) {
    CstConstraintActionList res =  tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstConstraintAction(res, tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make((CstConstraintAction)getValue(e), tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ) );
      }
    }
    return res;
  }

  private CstVisitList buildCstVisitList(List<? extends ParserRuleContext> ctx) {
    CstVisitList res =  tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstVisit(res, tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make((CstVisit)getValue(e), tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() ) );
      }
    }
    return res;
  }

  private CstSlotList buildCstSlotList(List<? extends ParserRuleContext> ctx) {
    CstSlotList res =  tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstSlot(res, tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make((CstSlot)getValue(e), tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ) );
      }
    }
    return res;
  }

  private CstPatternList buildCstPatternList(List<? extends ParserRuleContext> ctx) {
    CstPatternList res =  tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstPattern(res, tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make((CstPattern)getValue(e), tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) );
      }
    }
    return res;
  }

  private CstTermList buildCstTermList(List<? extends ParserRuleContext> ctx) {
    CstTermList res =  tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstTerm(res, tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make((CstTerm)getValue(e), tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() ) );
      }
    }
    return res;
  }

  private CstSymbolList buildCstSymbolList(List<? extends ParserRuleContext> ctx) {
    CstSymbolList res =  tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = tom_append_list_ConcCstSymbol(res, tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make((CstSymbol)getValue(e), tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ) );
      }
    }
    return res;
  }

  /*
   * add missing spaces/newlines between two tokens
   */
  private static String betweenToken(Token t1, Token t2) {
    String newline = System.getProperty("line.separator");
    int l = t1.getLine();
    int c = t1.	getCharPositionInLine()+1;
    String res = "";
    while(l < t2.getLine()) {
      res += newline;
      l++;
      c = 1;
    }
    while(c < t2.	getCharPositionInLine()) {
      res += " ";
      c++;
    }
    return res;
  }
}

