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

package tom.engine.parser.tomjava;

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

import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.rule.types.*;
//import tom.library.sl.*;

/*
 * CST builder
 * traverse the ANTLR tree and generate a Gom Cst_Program, of sort CstProgram
 */
public class CstBuilder extends TomJavaParserBaseListener {
     private static   tom.engine.adt.cst.types.CstPatternList  tom_append_list_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList l1,  tom.engine.adt.cst.types.CstPatternList  l2) {     if( l1.isEmptyConcCstPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPattern() ) {       return l1;     } else if(  l1.getTailConcCstPattern() .isEmptyConcCstPattern() ) {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,tom_append_list_ConcCstPattern( l1.getTailConcCstPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPatternList  tom_get_slice_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList  begin,  tom.engine.adt.cst.types.CstPatternList  end, tom.engine.adt.cst.types.CstPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPattern()  ||  (end== tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( begin.getHeadConcCstPattern() ,( tom.engine.adt.cst.types.CstPatternList )tom_get_slice_ConcCstPattern( begin.getTailConcCstPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstVisitList  tom_append_list_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList l1,  tom.engine.adt.cst.types.CstVisitList  l2) {     if( l1.isEmptyConcCstVisit() ) {       return l2;     } else if( l2.isEmptyConcCstVisit() ) {       return l1;     } else if(  l1.getTailConcCstVisit() .isEmptyConcCstVisit() ) {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,tom_append_list_ConcCstVisit( l1.getTailConcCstVisit() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstVisitList  tom_get_slice_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList  begin,  tom.engine.adt.cst.types.CstVisitList  end, tom.engine.adt.cst.types.CstVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstVisit()  ||  (end== tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( begin.getHeadConcCstVisit() ,( tom.engine.adt.cst.types.CstVisitList )tom_get_slice_ConcCstVisit( begin.getTailConcCstVisit() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSymbolList  tom_append_list_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList l1,  tom.engine.adt.cst.types.CstSymbolList  l2) {     if( l1.isEmptyConcCstSymbol() ) {       return l2;     } else if( l2.isEmptyConcCstSymbol() ) {       return l1;     } else if(  l1.getTailConcCstSymbol() .isEmptyConcCstSymbol() ) {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,tom_append_list_ConcCstSymbol( l1.getTailConcCstSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSymbolList  tom_get_slice_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList  begin,  tom.engine.adt.cst.types.CstSymbolList  end, tom.engine.adt.cst.types.CstSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSymbol()  ||  (end== tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( begin.getHeadConcCstSymbol() ,( tom.engine.adt.cst.types.CstSymbolList )tom_get_slice_ConcCstSymbol( begin.getTailConcCstSymbol() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstNameList  tom_append_list_ConcCstName( tom.engine.adt.cst.types.CstNameList l1,  tom.engine.adt.cst.types.CstNameList  l2) {     if( l1.isEmptyConcCstName() ) {       return l2;     } else if( l2.isEmptyConcCstName() ) {       return l1;     } else if(  l1.getTailConcCstName() .isEmptyConcCstName() ) {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,tom_append_list_ConcCstName( l1.getTailConcCstName() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstNameList  tom_get_slice_ConcCstName( tom.engine.adt.cst.types.CstNameList  begin,  tom.engine.adt.cst.types.CstNameList  end, tom.engine.adt.cst.types.CstNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstName()  ||  (end== tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( begin.getHeadConcCstName() ,( tom.engine.adt.cst.types.CstNameList )tom_get_slice_ConcCstName( begin.getTailConcCstName() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_append_list_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList l1,  tom.engine.adt.cst.types.CstPairSlotBQTermList  l2) {     if( l1.isEmptyConcCstPairSlotBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstPairSlotBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstPairSlotBQTerm() .isEmptyConcCstPairSlotBQTerm() ) {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,tom_append_list_ConcCstPairSlotBQTerm( l1.getTailConcCstPairSlotBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_get_slice_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList  begin,  tom.engine.adt.cst.types.CstPairSlotBQTermList  end, tom.engine.adt.cst.types.CstPairSlotBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairSlotBQTerm()  ||  (end== tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( begin.getHeadConcCstPairSlotBQTerm() ,( tom.engine.adt.cst.types.CstPairSlotBQTermList )tom_get_slice_ConcCstPairSlotBQTerm( begin.getTailConcCstPairSlotBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSlotList  tom_append_list_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList l1,  tom.engine.adt.cst.types.CstSlotList  l2) {     if( l1.isEmptyConcCstSlot() ) {       return l2;     } else if( l2.isEmptyConcCstSlot() ) {       return l1;     } else if(  l1.getTailConcCstSlot() .isEmptyConcCstSlot() ) {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,tom_append_list_ConcCstSlot( l1.getTailConcCstSlot() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSlotList  tom_get_slice_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList  begin,  tom.engine.adt.cst.types.CstSlotList  end, tom.engine.adt.cst.types.CstSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSlot()  ||  (end== tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( begin.getHeadConcCstSlot() ,( tom.engine.adt.cst.types.CstSlotList )tom_get_slice_ConcCstSlot( begin.getTailConcCstSlot() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBlockList  tom_append_list_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList l1,  tom.engine.adt.cst.types.CstBlockList  l2) {     if( l1.isEmptyConcCstBlock() ) {       return l2;     } else if( l2.isEmptyConcCstBlock() ) {       return l1;     } else if(  l1.getTailConcCstBlock() .isEmptyConcCstBlock() ) {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,tom_append_list_ConcCstBlock( l1.getTailConcCstBlock() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBlockList  tom_get_slice_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList  begin,  tom.engine.adt.cst.types.CstBlockList  end, tom.engine.adt.cst.types.CstBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBlock()  ||  (end== tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( begin.getHeadConcCstBlock() ,( tom.engine.adt.cst.types.CstBlockList )tom_get_slice_ConcCstBlock( begin.getTailConcCstBlock() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_AndConstraint() ) {       return l2;     } else if( l2.isEmptyCst_AndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {       if(  l1.getTailCst_AndConstraint() .isEmptyCst_AndConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,tom_append_list_Cst_AndConstraint( l1.getTailCst_AndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_AndConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getHeadCst_AndConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_AndConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getTailCst_AndConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_OrConstraint() ) {       return l2;     } else if( l2.isEmptyCst_OrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {       if(  l1.getTailCst_OrConstraint() .isEmptyCst_OrConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,tom_append_list_Cst_OrConstraint( l1.getTailCst_OrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_OrConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getHeadCst_OrConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_OrConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getTailCst_OrConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairPatternList  tom_append_list_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList l1,  tom.engine.adt.cst.types.CstPairPatternList  l2) {     if( l1.isEmptyConcCstPairPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPairPattern() ) {       return l1;     } else if(  l1.getTailConcCstPairPattern() .isEmptyConcCstPairPattern() ) {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,tom_append_list_ConcCstPairPattern( l1.getTailConcCstPairPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairPatternList  tom_get_slice_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList  begin,  tom.engine.adt.cst.types.CstPairPatternList  end, tom.engine.adt.cst.types.CstPairPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairPattern()  ||  (end== tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( begin.getHeadConcCstPairPattern() ,( tom.engine.adt.cst.types.CstPairPatternList )tom_get_slice_ConcCstPairPattern( begin.getTailConcCstPairPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOperatorList  tom_append_list_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList l1,  tom.engine.adt.cst.types.CstOperatorList  l2) {     if( l1.isEmptyConcCstOperator() ) {       return l2;     } else if( l2.isEmptyConcCstOperator() ) {       return l1;     } else if(  l1.getTailConcCstOperator() .isEmptyConcCstOperator() ) {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,tom_append_list_ConcCstOperator( l1.getTailConcCstOperator() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOperatorList  tom_get_slice_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList  begin,  tom.engine.adt.cst.types.CstOperatorList  end, tom.engine.adt.cst.types.CstOperatorList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOperator()  ||  (end== tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( begin.getHeadConcCstOperator() ,( tom.engine.adt.cst.types.CstOperatorList )tom_get_slice_ConcCstOperator( begin.getTailConcCstOperator() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOptionList  tom_append_list_ConcCstOption( tom.engine.adt.cst.types.CstOptionList l1,  tom.engine.adt.cst.types.CstOptionList  l2) {     if( l1.isEmptyConcCstOption() ) {       return l2;     } else if( l2.isEmptyConcCstOption() ) {       return l1;     } else if(  l1.getTailConcCstOption() .isEmptyConcCstOption() ) {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,tom_append_list_ConcCstOption( l1.getTailConcCstOption() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOptionList  tom_get_slice_ConcCstOption( tom.engine.adt.cst.types.CstOptionList  begin,  tom.engine.adt.cst.types.CstOptionList  end, tom.engine.adt.cst.types.CstOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOption()  ||  (end== tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( begin.getHeadConcCstOption() ,( tom.engine.adt.cst.types.CstOptionList )tom_get_slice_ConcCstOption( begin.getTailConcCstOption() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_append_list_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList l1,  tom.engine.adt.cst.types.CstConstraintActionList  l2) {     if( l1.isEmptyConcCstConstraintAction() ) {       return l2;     } else if( l2.isEmptyConcCstConstraintAction() ) {       return l1;     } else if(  l1.getTailConcCstConstraintAction() .isEmptyConcCstConstraintAction() ) {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,tom_append_list_ConcCstConstraintAction( l1.getTailConcCstConstraintAction() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_get_slice_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList  begin,  tom.engine.adt.cst.types.CstConstraintActionList  end, tom.engine.adt.cst.types.CstConstraintActionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstConstraintAction()  ||  (end== tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( begin.getHeadConcCstConstraintAction() ,( tom.engine.adt.cst.types.CstConstraintActionList )tom_get_slice_ConcCstConstraintAction( begin.getTailConcCstConstraintAction() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBQTermList  tom_append_list_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList l1,  tom.engine.adt.cst.types.CstBQTermList  l2) {     if( l1.isEmptyConcCstBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstBQTerm() .isEmptyConcCstBQTerm() ) {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,tom_append_list_ConcCstBQTerm( l1.getTailConcCstBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBQTermList  tom_get_slice_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList  begin,  tom.engine.adt.cst.types.CstBQTermList  end, tom.engine.adt.cst.types.CstBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBQTerm()  ||  (end== tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( begin.getHeadConcCstBQTerm() ,( tom.engine.adt.cst.types.CstBQTermList )tom_get_slice_ConcCstBQTerm( begin.getTailConcCstBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstTermList  tom_append_list_ConcCstTerm( tom.engine.adt.cst.types.CstTermList l1,  tom.engine.adt.cst.types.CstTermList  l2) {     if( l1.isEmptyConcCstTerm() ) {       return l2;     } else if( l2.isEmptyConcCstTerm() ) {       return l1;     } else if(  l1.getTailConcCstTerm() .isEmptyConcCstTerm() ) {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,tom_append_list_ConcCstTerm( l1.getTailConcCstTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstTermList  tom_get_slice_ConcCstTerm( tom.engine.adt.cst.types.CstTermList  begin,  tom.engine.adt.cst.types.CstTermList  end, tom.engine.adt.cst.types.CstTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstTerm()  ||  (end== tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( begin.getHeadConcCstTerm() ,( tom.engine.adt.cst.types.CstTermList )tom_get_slice_ConcCstTerm( begin.getTailConcCstTerm() ,end,tail)) ;   }      private static   tom.gom.adt.rule.types.RuleList  tom_append_list_RuleList( tom.gom.adt.rule.types.RuleList l1,  tom.gom.adt.rule.types.RuleList  l2) {     if( l1.isEmptyRuleList() ) {       return l2;     } else if( l2.isEmptyRuleList() ) {       return l1;     } else if(  l1.getTailRuleList() .isEmptyRuleList() ) {       return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( l1.getHeadRuleList() ,l2) ;     } else {       return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( l1.getHeadRuleList() ,tom_append_list_RuleList( l1.getTailRuleList() ,l2)) ;     }   }   private static   tom.gom.adt.rule.types.RuleList  tom_get_slice_RuleList( tom.gom.adt.rule.types.RuleList  begin,  tom.gom.adt.rule.types.RuleList  end, tom.gom.adt.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyRuleList()  ||  (end== tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( begin.getHeadRuleList() ,( tom.gom.adt.rule.types.RuleList )tom_get_slice_RuleList( begin.getTailRuleList() ,end,tail)) ;   }      private static   tom.gom.adt.rule.types.TermList  tom_append_list_TermList( tom.gom.adt.rule.types.TermList l1,  tom.gom.adt.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  tom.gom.adt.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  tom.gom.adt.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   tom.gom.adt.rule.types.TermList  tom_get_slice_TermList( tom.gom.adt.rule.types.TermList  begin,  tom.gom.adt.rule.types.TermList  end, tom.gom.adt.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end== tom.gom.adt.rule.types.termlist.EmptyTermList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( tom.gom.adt.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }      private static   tom.gom.adt.rule.types.Condition  tom_append_list_CondAnd( tom.gom.adt.rule.types.Condition  l1,  tom.gom.adt.rule.types.Condition  l2) {     if( l1.isEmptyCondAnd() ) {       return l2;     } else if( l2.isEmptyCondAnd() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || (l1 instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) ) {       if(  l1.getTailCondAnd() .isEmptyCondAnd() ) {         return  tom.gom.adt.rule.types.condition.ConsCondAnd.make( l1.getHeadCondAnd() ,l2) ;       } else {         return  tom.gom.adt.rule.types.condition.ConsCondAnd.make( l1.getHeadCondAnd() ,tom_append_list_CondAnd( l1.getTailCondAnd() ,l2)) ;       }     } else {       return  tom.gom.adt.rule.types.condition.ConsCondAnd.make(l1,l2) ;     }   }   private static   tom.gom.adt.rule.types.Condition  tom_get_slice_CondAnd( tom.gom.adt.rule.types.Condition  begin,  tom.gom.adt.rule.types.Condition  end, tom.gom.adt.rule.types.Condition  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCondAnd()  ||  (end== tom.gom.adt.rule.types.condition.EmptyCondAnd.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.condition.ConsCondAnd.make((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) )? begin.getHeadCondAnd() :begin),( tom.gom.adt.rule.types.Condition )tom_get_slice_CondAnd((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) )? begin.getTailCondAnd() : tom.gom.adt.rule.types.condition.EmptyCondAnd.make() ),end,tail)) ;   }      private static   tom.gom.adt.rule.types.Condition  tom_append_list_CondOr( tom.gom.adt.rule.types.Condition  l1,  tom.gom.adt.rule.types.Condition  l2) {     if( l1.isEmptyCondOr() ) {       return l2;     } else if( l2.isEmptyCondOr() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || (l1 instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) ) {       if(  l1.getTailCondOr() .isEmptyCondOr() ) {         return  tom.gom.adt.rule.types.condition.ConsCondOr.make( l1.getHeadCondOr() ,l2) ;       } else {         return  tom.gom.adt.rule.types.condition.ConsCondOr.make( l1.getHeadCondOr() ,tom_append_list_CondOr( l1.getTailCondOr() ,l2)) ;       }     } else {       return  tom.gom.adt.rule.types.condition.ConsCondOr.make(l1,l2) ;     }   }   private static   tom.gom.adt.rule.types.Condition  tom_get_slice_CondOr( tom.gom.adt.rule.types.Condition  begin,  tom.gom.adt.rule.types.Condition  end, tom.gom.adt.rule.types.Condition  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCondOr()  ||  (end== tom.gom.adt.rule.types.condition.EmptyCondOr.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.condition.ConsCondOr.make((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) )? begin.getHeadCondOr() :begin),( tom.gom.adt.rule.types.Condition )tom_get_slice_CondOr((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) )? begin.getTailCondOr() : tom.gom.adt.rule.types.condition.EmptyCondOr.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.Option  tom_append_list_OptionList( tom.gom.adt.gom.types.Option  l1,  tom.gom.adt.gom.types.Option  l2) {     if( l1.isEmptyOptionList() ) {       return l2;     } else if( l2.isEmptyOptionList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) ) {       if(  l1.getTailOptionList() .isEmptyOptionList() ) {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,l2) ;       } else {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,tom_append_list_OptionList( l1.getTailOptionList() ,l2)) ;       }     } else {       return  tom.gom.adt.gom.types.option.ConsOptionList.make(l1,l2) ;     }   }   private static   tom.gom.adt.gom.types.Option  tom_get_slice_OptionList( tom.gom.adt.gom.types.Option  begin,  tom.gom.adt.gom.types.Option  end, tom.gom.adt.gom.types.Option  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOptionList()  ||  (end== tom.gom.adt.gom.types.option.EmptyOptionList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getHeadOptionList() :begin),( tom.gom.adt.gom.types.Option )tom_get_slice_OptionList((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.AtomList  tom_append_list_ConcAtom( tom.gom.adt.gom.types.AtomList l1,  tom.gom.adt.gom.types.AtomList  l2) {     if( l1.isEmptyConcAtom() ) {       return l2;     } else if( l2.isEmptyConcAtom() ) {       return l1;     } else if(  l1.getTailConcAtom() .isEmptyConcAtom() ) {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,l2) ;     } else {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,tom_append_list_ConcAtom( l1.getTailConcAtom() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AtomList  tom_get_slice_ConcAtom( tom.gom.adt.gom.types.AtomList  begin,  tom.gom.adt.gom.types.AtomList  end, tom.gom.adt.gom.types.AtomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAtom()  ||  (end== tom.gom.adt.gom.types.atomlist.EmptyConcAtom.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( begin.getHeadConcAtom() ,( tom.gom.adt.gom.types.AtomList )tom_get_slice_ConcAtom( begin.getTailConcAtom() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ImportList  tom_append_list_ConcImportedModule( tom.gom.adt.gom.types.ImportList l1,  tom.gom.adt.gom.types.ImportList  l2) {     if( l1.isEmptyConcImportedModule() ) {       return l2;     } else if( l2.isEmptyConcImportedModule() ) {       return l1;     } else if(  l1.getTailConcImportedModule() .isEmptyConcImportedModule() ) {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,tom_append_list_ConcImportedModule( l1.getTailConcImportedModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ImportList  tom_get_slice_ConcImportedModule( tom.gom.adt.gom.types.ImportList  begin,  tom.gom.adt.gom.types.ImportList  end, tom.gom.adt.gom.types.ImportList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcImportedModule()  ||  (end== tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( begin.getHeadConcImportedModule() ,( tom.gom.adt.gom.types.ImportList )tom_get_slice_ConcImportedModule( begin.getTailConcImportedModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ArgList  tom_append_list_ConcArg( tom.gom.adt.gom.types.ArgList l1,  tom.gom.adt.gom.types.ArgList  l2) {     if( l1.isEmptyConcArg() ) {       return l2;     } else if( l2.isEmptyConcArg() ) {       return l1;     } else if(  l1.getTailConcArg() .isEmptyConcArg() ) {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,l2) ;     } else {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,tom_append_list_ConcArg( l1.getTailConcArg() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ArgList  tom_get_slice_ConcArg( tom.gom.adt.gom.types.ArgList  begin,  tom.gom.adt.gom.types.ArgList  end, tom.gom.adt.gom.types.ArgList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcArg()  ||  (end== tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( begin.getHeadConcArg() ,( tom.gom.adt.gom.types.ArgList )tom_get_slice_ConcArg( begin.getTailConcArg() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }   



  private String filename;
  private BufferedTokenStream tokens;
  private Set<Token> usedToken; // used to add spaces before of after a given token
  private GomStreamManager streamManager;

  public CstBuilder(String filename, BufferedTokenStream tokens) {
    this.filename = filename;
    this.tokens = tokens;
    this.usedToken = new HashSet<Token>();
  }
  
  public CstBuilder(String filename, BufferedTokenStream tokens, GomStreamManager streamManager) {
    this.filename = filename;
    this.tokens = tokens;
    this.usedToken = new HashSet<Token>();
    this.streamManager = streamManager;
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

  public void exitCompilationUnit(TomJavaParser.CompilationUnitContext ctx) {
    setValue("exitCompilationUnit", ctx, buildBlockList(ctx));
  }
  
  public void exitDeclarationsUnit(TomJavaParser.DeclarationsUnitContext ctx) {
    setValue("exitDeclarationsUnit", ctx, buildBlockList(ctx));
  }
  
  public void exitExpressionUnit(TomJavaParser.ExpressionUnitContext ctx) {
    setValue("exitExpressionUnit", ctx, buildBlockList(ctx));
  }
  
  /*
   * gomUnit
   *   : module EOF
   *   ;
   */
  public void exitGomUnit(TomJavaParser.GomUnitContext ctx) {
    setValue("exitGomUnit", ctx, getValue(ctx.module()));
  }

  public void exitPackageDeclaration(TomJavaParser.PackageDeclarationContext ctx) {
    setValue("exitPackageDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitImportDeclaration(TomJavaParser.ImportDeclarationContext ctx) {
    setValue("exitImportDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitTypeDeclaration(TomJavaParser.TypeDeclarationContext ctx) {
    setValue("exitTypeDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitModifier(TomJavaParser.ModifierContext ctx) {
    setValue("exitModifier", ctx, buildBlockList(ctx));
  }

  public void exitClassOrInterfaceModifier(TomJavaParser.ClassOrInterfaceModifierContext ctx) {
    setValue("exitClassOrInterfaceModifier", ctx, buildBlockList(ctx));
  }

  public void exitVariableModifier(TomJavaParser.VariableModifierContext ctx) {
    setValue("exitVariableModifier", ctx, buildBlockList(ctx));
  }

  public void exitClassDeclaration(TomJavaParser.ClassDeclarationContext ctx) {
    setValue("exitClassDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitTypeParameters(TomJavaParser.TypeParametersContext ctx) {
    setValue("exitTypeParameters", ctx, buildBlockList(ctx));
  }

  public void exitTypeParameter(TomJavaParser.TypeParameterContext ctx) {
    setValue("exitTypeParameter", ctx, buildBlockList(ctx));
  }

  public void exitTypeBound(TomJavaParser.TypeBoundContext ctx) {
    setValue("exitTypeBound", ctx, buildBlockList(ctx));
  }

  public void exitEnumDeclaration(TomJavaParser.EnumDeclarationContext ctx) {
    setValue("exitEnumDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitEnumConstants(TomJavaParser.EnumConstantsContext ctx) {
    setValue("exitEnumConstants", ctx, buildBlockList(ctx));
  }

  public void exitEnumConstant(TomJavaParser.EnumConstantContext ctx) {
    setValue("exitEnumConstant", ctx, buildBlockList(ctx));
  }

  public void exitEnumBodyDeclarations(TomJavaParser.EnumBodyDeclarationsContext ctx) {
    setValue("exitEnumBodyDeclarations", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceDeclaration(TomJavaParser.InterfaceDeclarationContext ctx) {
    setValue("exitInterfaceDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitClassBody(TomJavaParser.ClassBodyContext ctx) {
    setValue("exitClassBody", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceBody(TomJavaParser.InterfaceBodyContext ctx) {
    setValue("exitInterfaceBody", ctx, buildBlockList(ctx));
  }

  public void exitClassBodyDeclaration(TomJavaParser.ClassBodyDeclarationContext ctx) {
    setValue("exitClassBodyDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitMemberDeclaration(TomJavaParser.MemberDeclarationContext ctx) {
    setValue("exitMemberDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitMethodDeclaration(TomJavaParser.MethodDeclarationContext ctx) {
    setValue("exitMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitMethodBody(TomJavaParser.MethodBodyContext ctx) {
    setValue("exitMethodBody", ctx, buildBlockList(ctx));
  }

  public void exitTypeTypeOrVoid(TomJavaParser.TypeTypeOrVoidContext ctx) {
    setValue("exitTypeTypeOrVoid", ctx, buildBlockList(ctx));
  }

  public void exitGenericMethodDeclaration(TomJavaParser.GenericMethodDeclarationContext ctx) {
    setValue("exitGenericMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitGenericConstructorDeclaration(TomJavaParser.GenericConstructorDeclarationContext ctx) {
    setValue("exitGenericConstructorDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitConstructorDeclaration(TomJavaParser.ConstructorDeclarationContext ctx) {
    setValue("exitConstructorDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitFieldDeclaration(TomJavaParser.FieldDeclarationContext ctx) {
    setValue("exitFieldDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceBodyDeclaration(TomJavaParser.InterfaceBodyDeclarationContext ctx) {
    setValue("exitInterfaceBodyDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceMemberDeclaration(TomJavaParser.InterfaceMemberDeclarationContext ctx) {
    setValue("exitInterfaceMemberDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitConstDeclaration(TomJavaParser.ConstDeclarationContext ctx) {
    setValue("exitConstDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitConstantDeclarator(TomJavaParser.ConstantDeclaratorContext ctx) {
    setValue("exitConstantDeclarator", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceMethodDeclaration(TomJavaParser.InterfaceMethodDeclarationContext ctx) {
    setValue("exitInterfaceMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceMethodModifier(TomJavaParser.InterfaceMethodModifierContext ctx) {
    setValue("exitInterfaceMethodModifier", ctx, buildBlockList(ctx));
  }

  public void exitGenericInterfaceMethodDeclaration(TomJavaParser.GenericInterfaceMethodDeclarationContext ctx) {
    setValue("exitGenericInterfaceMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitVariableDeclarators(TomJavaParser.VariableDeclaratorsContext ctx) {
    setValue("exitVariableDeclarators", ctx, buildBlockList(ctx));
  }

  public void exitVariableDeclarator(TomJavaParser.VariableDeclaratorContext ctx) {
    setValue("exitVariableDeclarator", ctx, buildBlockList(ctx));
  }

  public void exitVariableDeclaratorId(TomJavaParser.VariableDeclaratorIdContext ctx) {
    setValue("exitVariableDeclaratorId", ctx, buildBlockList(ctx));
  }

  public void exitVariableInitializer(TomJavaParser.VariableInitializerContext ctx) {
    setValue("exitVariableInitializer", ctx, buildBlockList(ctx));
  }

  public void exitArrayInitializer(TomJavaParser.ArrayInitializerContext ctx) {
    setValue("exitArrayInitializer", ctx, buildBlockList(ctx));
  }

  public void exitClassOrInterfaceType(TomJavaParser.ClassOrInterfaceTypeContext ctx) {
    setValue("exitClassOrInterfaceType", ctx, buildBlockList(ctx));
  }

  public void exitTypeArgument(TomJavaParser.TypeArgumentContext ctx) {
    setValue("exitTypeArgument", ctx, buildBlockList(ctx));
  }

  public void exitQualifiedNameList(TomJavaParser.QualifiedNameListContext ctx) {
    setValue("exitQualifiedNameList", ctx, buildBlockList(ctx));
  }

  public void exitFormalParameters(TomJavaParser.FormalParametersContext ctx) {
    setValue("exitFormalParameters", ctx, buildBlockList(ctx));
  }

  public void exitFormalParameterList(TomJavaParser.FormalParameterListContext ctx) {
    setValue("exitFormalParameterList", ctx, buildBlockList(ctx));
  }

  public void exitFormalParameter(TomJavaParser.FormalParameterContext ctx) {
    setValue("exitFormalParameter", ctx, buildBlockList(ctx));
  }

  public void exitLastFormalParameter(TomJavaParser.LastFormalParameterContext ctx) {
    setValue("exitLastFormalParameter", ctx, buildBlockList(ctx));
  }

  public void exitQualifiedName(TomJavaParser.QualifiedNameContext ctx) {
    setValue("exitQualifiedName", ctx, buildBlockList(ctx));
  }

  public void exitLiteral(TomJavaParser.LiteralContext ctx) {
    setValue("exitLiteral", ctx, buildBlockList(ctx));
  }

  public void exitIntegerLiteral(TomJavaParser.IntegerLiteralContext ctx) {
    setValue("exitIntegerLiteral", ctx, buildBlockList(ctx));
  }

  public void exitFloatLiteral(TomJavaParser.FloatLiteralContext ctx) {
    setValue("exitFloatLiteral", ctx, buildBlockList(ctx));
  }

  public void exitAnnotation(TomJavaParser.AnnotationContext ctx) {
    setValue("exitAnnotation", ctx, buildBlockList(ctx));
  }

  public void exitElementValuePairs(TomJavaParser.ElementValuePairsContext ctx) {
    setValue("exitElementValuePairs", ctx, buildBlockList(ctx));
  }

  public void exitElementValuePair(TomJavaParser.ElementValuePairContext ctx) {
    setValue("exitElementValuePair", ctx, buildBlockList(ctx));
  }

  public void exitElementValue(TomJavaParser.ElementValueContext ctx) {
    setValue("exitElementValue", ctx, buildBlockList(ctx));
  }

  public void exitElementValueArrayInitializer(TomJavaParser.ElementValueArrayInitializerContext ctx) {
    setValue("exitElementValueArrayInitializer", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeDeclaration(TomJavaParser.AnnotationTypeDeclarationContext ctx) {
    setValue("exitAnnotationTypeDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeBody(TomJavaParser.AnnotationTypeBodyContext ctx) {
    setValue("exitAnnotationTypeBody", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeElementDeclaration(TomJavaParser.AnnotationTypeElementDeclarationContext ctx) {
    setValue("exitAnnotationTypeElementDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeElementRest(TomJavaParser.AnnotationTypeElementRestContext ctx) {
    setValue("exitAnnotationTypeElementRest", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationMethodOrConstantRest(TomJavaParser.AnnotationMethodOrConstantRestContext ctx) {
    setValue("exitAnnotationMethodOrConstantRest", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationMethodRest(TomJavaParser.AnnotationMethodRestContext ctx) {
    setValue("exitAnnotationMethodRest", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationConstantRest(TomJavaParser.AnnotationConstantRestContext ctx) {
    setValue("exitAnnotationConstantRest", ctx, buildBlockList(ctx));
  }

  public void exitDefaultValue(TomJavaParser.DefaultValueContext ctx) {
    setValue("exitDefaultValue", ctx, buildBlockList(ctx));
  }

  public void exitBlock(TomJavaParser.BlockContext ctx) {
    setValue("exitBlock", ctx, buildBlockList(ctx));
  }

  public void exitBlockStatement(TomJavaParser.BlockStatementContext ctx) {
    setValue("exitBlockStatement", ctx, buildBlockList(ctx));
  }

  public void exitLocalVariableDeclaration(TomJavaParser.LocalVariableDeclarationContext ctx) {
    setValue("exitLocalVariableDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitLocalTypeDeclaration(TomJavaParser.LocalTypeDeclarationContext ctx) {
    setValue("exitLocalTypeDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitStatement(TomJavaParser.StatementContext ctx) {
    setValue("exitStatement", ctx, buildBlockList(ctx));
  }

  public void exitCatchClause(TomJavaParser.CatchClauseContext ctx) {
    setValue("exitCatchClause", ctx, buildBlockList(ctx));
  }

  public void exitCatchType(TomJavaParser.CatchTypeContext ctx) {
    setValue("exitCatchType", ctx, buildBlockList(ctx));
  }

  public void exitFinallyBlock(TomJavaParser.FinallyBlockContext ctx) {
    setValue("exitFinallyBlock", ctx, buildBlockList(ctx));
  }

  public void exitResourceSpecification(TomJavaParser.ResourceSpecificationContext ctx) {
    setValue("exitResourceSpecification", ctx, buildBlockList(ctx));
  }

  public void exitResources(TomJavaParser.ResourcesContext ctx) {
    setValue("exitResources", ctx, buildBlockList(ctx));
  }

  public void exitResource(TomJavaParser.ResourceContext ctx) {
    setValue("exitResource", ctx, buildBlockList(ctx));
  }

  public void exitSwitchBlockStatementGroup(TomJavaParser.SwitchBlockStatementGroupContext ctx) {
    setValue("exitSwitchBlockStatementGroup", ctx, buildBlockList(ctx));
  }

  public void exitSwitchLabel(TomJavaParser.SwitchLabelContext ctx) {
    setValue("exitSwitchLabel", ctx, buildBlockList(ctx));
  }

  public void exitForControl(TomJavaParser.ForControlContext ctx) {
    setValue("exitForControl", ctx, buildBlockList(ctx));
  }

  public void exitForInit(TomJavaParser.ForInitContext ctx) {
    setValue("exitForInit", ctx, buildBlockList(ctx));
  }

  public void exitEnhancedForControl(TomJavaParser.EnhancedForControlContext ctx) {
    setValue("exitEnhancedForControl", ctx, buildBlockList(ctx));
  }

  public void exitParExpression(TomJavaParser.ParExpressionContext ctx) {
    setValue("exitParExpression", ctx, buildBlockList(ctx));
  }

  public void exitExpressionList(TomJavaParser.ExpressionListContext ctx) {
    setValue("exitExpressionList", ctx, buildBlockList(ctx));
  }

  public void exitExpression(TomJavaParser.ExpressionContext ctx) {
    setValue("exitExpression", ctx, buildBlockList(ctx));
  }
  
  public void exitFunTerm(TomJavaParser.FunTermContext ctx) {
    setValue("exitFunTerm", ctx, buildBlockList(ctx));
  }

  public void exitLambdaExpression(TomJavaParser.LambdaExpressionContext ctx) {
    setValue("exitLambdaExpression", ctx, buildBlockList(ctx));
  }

  public void exitLambdaParameters(TomJavaParser.LambdaParametersContext ctx) {
    setValue("exitLambdaParameters", ctx, buildBlockList(ctx));
  }

  public void exitLambdaBody(TomJavaParser.LambdaBodyContext ctx) {
    setValue("exitLambdaBody", ctx, buildBlockList(ctx));
  }

  public void exitPrimary(TomJavaParser.PrimaryContext ctx) {
    setValue("exitPrimary", ctx, buildBlockList(ctx));
  }

  public void exitClassType(TomJavaParser.ClassTypeContext ctx) {
    setValue("exitClassType", ctx, buildBlockList(ctx));
  }

  public void exitCreator(TomJavaParser.CreatorContext ctx) {
    setValue("exitCreator", ctx, buildBlockList(ctx));
  }

  public void exitCreatedName(TomJavaParser.CreatedNameContext ctx) {
    setValue("exitCreatedName", ctx, buildBlockList(ctx));
  }

  public void exitInnerCreator(TomJavaParser.InnerCreatorContext ctx) {
    setValue("exitInnerCreator", ctx, buildBlockList(ctx));
  }

  public void exitArrayCreatorRest(TomJavaParser.ArrayCreatorRestContext ctx) {
    setValue("exitArrayCreatorRest", ctx, buildBlockList(ctx));
  }

  public void exitClassCreatorRest(TomJavaParser.ClassCreatorRestContext ctx) {
    setValue("exitClassCreatorRest", ctx, buildBlockList(ctx));
  }

  public void exitExplicitGenericInvocation(TomJavaParser.ExplicitGenericInvocationContext ctx) {
    setValue("exitExplicitGenericInvocation", ctx, buildBlockList(ctx));
  }

  public void exitTypeArgumentsOrDiamond(TomJavaParser.TypeArgumentsOrDiamondContext ctx) {
    setValue("exitTypeArgumentsOrDiamond", ctx, buildBlockList(ctx));
  }

  public void exitNonWildcardTypeArgumentsOrDiamond(TomJavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx) {
    setValue("exitNonWildcardTypeArgumentsOrDiamond", ctx, buildBlockList(ctx));
  }

  public void exitNonWildcardTypeArguments(TomJavaParser.NonWildcardTypeArgumentsContext ctx) {
    setValue("exitNonWildcardTypeArguments", ctx, buildBlockList(ctx));
  }

  public void exitTypeList(TomJavaParser.TypeListContext ctx) {
    setValue("exitTypeList", ctx, buildBlockList(ctx));
  }

  public void exitTypeType(TomJavaParser.TypeTypeContext ctx) {
    setValue("exitTypeType", ctx, buildBlockList(ctx));
  }

  public void exitPrimitiveType(TomJavaParser.PrimitiveTypeContext ctx) {
    setValue("exitPrimitiveType", ctx, buildBlockList(ctx));
  }

  public void exitTypeArguments(TomJavaParser.TypeArgumentsContext ctx) {
    setValue("exitTypeArguments", ctx, buildBlockList(ctx));
  }

  public void exitSuperSuffix(TomJavaParser.SuperSuffixContext ctx) {
    setValue("exitSuperSuffix", ctx, buildBlockList(ctx));
  }

  public void exitExplicitGenericInvocationSuffix(TomJavaParser.ExplicitGenericInvocationSuffixContext ctx) {
    setValue("exitExplicitGenericInvocationSuffix", ctx, buildBlockList(ctx));
  }

  public void exitArguments(TomJavaParser.ArgumentsContext ctx) {
    setValue("exitArguments", ctx, buildBlockList(ctx));
  }

//************************************TOM**************************************

  /*
   * tomDeclaration
   *  : strategyStatement
   *  | includeStatement
   *  | gomStatement
   *  | ruleStatement
   *  | typeterm
   *  | operator
   *  | oplist
   *  | oparray
   *  ;
   */
  public void exitTomDeclaration(TomJavaParser.TomDeclarationContext ctx) {
    setValue("exitTomDeclaration", ctx, getValue(ctx.getChild(0)));
  }
  
  /*
   * tomStatement
   *  : tomDeclaration
   *  | matchStatement
   *  ;
   */
  public void exitTomStatement(TomJavaParser.TomStatementContext ctx) {
    setValue("exitTomStatement", ctx, getValue(ctx.getChild(0)));
  }
  
  /*
   * tomTerm
   *  : bqcomposite
   *  | metaquote
   *  ;
   */
  public void exitTomTerm(TomJavaParser.TomTermContext ctx) {
    setValue("exitTomTerm", ctx, getValue(ctx.getChild(0)));
  }

  /* 
   * metaquote
   *   : METAQUOTE
   *   ;
   */
  public void exitMetaquote(TomJavaParser.MetaquoteContext ctx) {
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
  public void exitMatchStatement(TomJavaParser.MatchStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBQTermList subjectList = buildCstBQTermList(ctx.bqterm());
    CstConstraintActionList constraintActionList = buildCstConstraintActionList(ctx.actionRule());
    CstBlock res =  tom.engine.adt.cst.types.cstblock.Cst_MatchConstruct.make(optionList, subjectList, constraintActionList) ;
    setValue("exitMatchStatement", ctx,res);
  }

  /*
   * strategyStatement
   *   : STRATEGY tomIdentifier LPAREN slotList? RPAREN EXTENDS bqterm LBRACE visit* RBRACE
   *   ;
   */
  public void exitStrategyStatement(TomJavaParser.StrategyStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstName name =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) ;
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
   *   : INCLUDE LBRACE (DOT* SLASH)? tomIdentifier ((DOT|SLASH|BACKSLASH) tomIdentifier)*  RBRACE 
   *   ;
   */
  public void exitIncludeStatement(TomJavaParser.IncludeStatementContext ctx) {
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
   *   : GOM gomOptions? unknownBlock
   *   ;
   */
  public void exitGomStatement(TomJavaParser.GomStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    String text = ctx.unknownBlock().getText();
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
   *   : OPTIONSTART DMINUSID (COMMA DMINUSID)* OPTIONEND
   *   ;
   */
  public void exitGomOptions(TomJavaParser.GomOptionsContext ctx) {
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    for(TerminalNode e:ctx.DMINUSID()) {
      nameList =  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(e.getText()) ,tom_append_list_ConcCstName(nameList, tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() )) ;
    }
    setValue("exitGomOptions", ctx, nameList.reverse());
  }
  
  /*
   * gomBlock
   *   : GOMSTART module GOMEND
   *   ;
   */
  public void exitGomBlock(TomJavaParser.GomBlockContext ctx) {
    setValue("exitGomBlock", ctx, getValue(ctx.module()));
  }

  /*
   * ruleStatement
   *  : RULE unknownBlock
   *  ;
   */
  public void exitRuleStatement(TomJavaParser.RuleStatementContext ctx) {
    
  }
  
  /*
   * unknownBlock
   *  : LBRACE (tomBlock | unknownBlock | .)*? RBRACE
   *  ;
   */
  public void exitUnknownBlock(TomJavaParser.UnknownBlockContext ctx) {
    /*
    CstBlockList bl = `ConcCstBlock();
    
    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);

      if(child instanceof TomJavaParser.UnknownBlockContext) {
        bl = `ConcCstBlock((CstBlock)getValue(child),bl*);
      } else {
        //ParserRuleContext prc = (ParserRuleContext)child;
        //CstOption ot = extractOption(prc.getStart());
        //bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        bl = `ConcCstBlock(buildHostblock((Token)child.getPayload()),bl*);
      }
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx,`Cst_UnamedBlock(ConcCstOption(otext),bl.reverse()));*/
  }

  /*
   * visit
   *   : VISIT tomIdentifier LBRACE actionRule* RBRACE
   *   ;
   */
  public void exitVisit(TomJavaParser.VisitContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstConstraintActionList l = buildCstConstraintActionList(ctx.actionRule());
    CstVisit res =  tom.engine.adt.cst.types.cstvisit.Cst_VisitTerm.make( tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.tomIdentifier().getText()) , l, optionList) ;
    setValue("exitVisit", ctx,res);
  }

  /*
   * actionRule
   *   : patternlist ((AND | OR) constraint)? ARROW tomBlock
   *   | patternlist ((AND | OR) constraint)? ARROW bqterm
   *   | c=constraint ARROW tomBlock
   *   | c=constraint ARROW bqterm
   *   ;
   */
  public void exitActionRule(TomJavaParser.ActionRuleContext ctx) {
    CstConstraintAction res = null;
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBlockList action = null;
    if(ctx.tomBlock() != null) {
      action = getBlockListFromBlock(ctx.tomBlock());
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
   * tomBlock 
   *   : LBRACE (tomBlock | blockStatement)*? RBRACE
   *   ;
   */
  public void exitTomBlock(TomJavaParser.TomBlockContext ctx) {
    CstBlockList blocks =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ;
    CstBlock current = null;

    for(int i = 1; i < ctx.getChildCount() - 1; i++) {
      ParserRuleContext child = (ParserRuleContext) ctx.getChild(i);

      if(child instanceof TomJavaParser.TomBlockContext) {
        if(current != null) {
          blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
          current = null;
        }
        blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((CstBlock)getValue(child),tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else {
        for(CstBlock block:((CstBlockList)getValue(child)).getCollectionConcCstBlock()) {
          boolean isHostBlock = false;

          { /* unamed block */{ /* unamed block */if ( (block instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )block) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {



              current = merge(current, block);
              isHostBlock = true;
            }}}}



          if(!isHostBlock) {
            if(current != null) {
              blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
              current = null;
            }
            blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(block,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
          }
        }
      }
    }
    if (current != null)
    {
      blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx, tom.engine.adt.cst.types.cstblock.Cst_UnamedBlock.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(otext, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , blocks.reverse()) );
  }

  /*
   * slotList
   *   : slot (COMMA slot)*
   *   ;
   */
  public void exitSlotList(TomJavaParser.SlotListContext ctx) {
    CstSlotList res = buildCstSlotList(ctx.slot());
    setValue("exitSlotList", ctx,res);
  }

  /*
   * slot
   *   : id1=tomIdentifier COLON? id2=tomIdentifier
   *   ;
   */
  public void exitSlot(TomJavaParser.SlotContext ctx) {
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
   *   : pattern (COMMA pattern)* (WHEN term (COMMA term)*)?
   *   ;
   */
  public void exitPatternlist(TomJavaParser.PatternlistContext ctx) {
    CstPatternList res = buildCstPatternList(ctx.pattern());
    setValue("exitPatternList", ctx,res);
  }

  /*
   * constraint
   *   : constraint AND constraint
   *   | constraint OR constraint
   *   | pattern match_symbol='<' '<' bqterm
   *   | term GT term
   *   | term GE term
   *   | term LT term
   *   | term LE term
   *   | term EQUAL term
   *   | term NOTEQUAL term
   *   | LPAREN c=constraint RPAREN
   *   ;
   */
  public void exitConstraint(TomJavaParser.ConstraintContext ctx) {
    CstConstraint res = null;
    if(ctx.AND() != null || ctx.OR() != null) {
      CstConstraint lhs = (CstConstraint)getValue(ctx.constraint(0));
      CstConstraint rhs = (CstConstraint)getValue(ctx.constraint(1));
      res = (ctx.AND() != null)? tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(lhs, tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(rhs, tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) ) : tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(lhs, tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(rhs, tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) ) ;
    } else if(ctx.match_symbol != null) {
      CstPattern lhs = (CstPattern)getValue(ctx.pattern());
      CstBQTerm rhs = (CstBQTerm)getValue(ctx.bqterm());
      CstType rhs_type = (CstType)getValue2(ctx.bqterm());
      res =  tom.engine.adt.cst.types.cstconstraint.Cst_MatchTermConstraint.make(lhs, rhs, rhs_type) ;
    } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
      res = (CstConstraint)getValue(ctx.c);
    } else {
      CstTerm lhs = (CstTerm)getValue(ctx.term(0));
      CstTerm rhs = (CstTerm)getValue(ctx.term(1));
      if(ctx.GT() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumGreaterThan.make(lhs, rhs) ; }
      else if(ctx.GE() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumGreaterOrEqualThan.make(lhs, rhs) ; }
      else if(ctx.LT() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumLessThan.make(lhs, rhs) ; }
      else if(ctx.LE() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_NumLessOrEqualThan.make(lhs, rhs) ; }
      else if(ctx.EQUAL() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_EqualTo.make(lhs, rhs) ; }
      else if(ctx.NOTEQUAL() != null) { res =  tom.engine.adt.cst.types.cstconstraint.Cst_Different.make(lhs, rhs) ; }
    }

    setValue("exitConstraint",ctx,res);
  }

  /*
   * term
   *   : var=tomIdentifier STAR?
   *   | fsym=tomIdentifier LPAREN (term (COMMA term)*)? RPAREN 
   *   | constant
   *   ;
   */
  public void exitTerm(TomJavaParser.TermContext ctx) {
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
   *   : codomain=tomIdentifier? BQUOTE? fsym=tomIdentifier LPAREN (bqterm (COMMA bqterm)*)? RPAREN
   *   | codomain=tomIdentifier? BQUOTE? fsym=tomIdentifier LBRACK (pairSlotBqterm (COMMA pairSlotBqterm)*)? RBRACK 
   *   | codomain=tomIdentifier? BQUOTE? var=tomIdentifier STAR?
   *   | codomain=tomIdentifier? constant
   *   | UNDERSCORE
   *   ;
   */
  public void exitBqterm(TomJavaParser.BqtermContext ctx) {
    CstBQTerm res = null;
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType type = (ctx.codomain != null)? tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) : tom.engine.adt.cst.types.csttype.Cst_TypeUnknown.make() ;

    if(ctx.fsym != null && ctx.LPAREN() != null) {
      CstBQTermList args = buildCstBQTermList(ctx.bqterm());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQAppl.make(optionList, ctx.fsym.getText(), args) ;
    } else if(ctx.fsym != null && ctx.LBRACK() != null) {
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
   *   : tomIdentifier EQUAL bqterm
   *   ;
   */
  public void exitPairSlotBqterm(TomJavaParser.PairSlotBqtermContext ctx) {
    CstPairSlotBQTerm res = null;
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstName slotName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) ;
    CstBQTerm bqterm = (CstBQTerm) getValue(ctx.bqterm());
    res =  tom.engine.adt.cst.types.cstpairslotbqterm.Cst_PairSlotBQTerm.make(optionList, slotName, bqterm) ;
    setValue("exitPairSlotBqterm",ctx,res);
  }

  /*
   * bqcomposite
   *   : BQUOTE fsym=tomIdentifier LBRACK (pairSlotBqterm (COMMA pairSlotBqterm)*)? RBRACK 
   *   | BQUOTE fsym=tomIdentifier LPAREN (composite (COMMA composite)*)? RPAREN
   *   | BQUOTE LPAREN sub=composite RPAREN
   *   | BQUOTE var=tomIdentifier STAR?
   *   ;
   */
  public void exitBqcomposite(TomJavaParser.BqcompositeContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBlock res = null;
    if(ctx.fsym != null && ctx.LBRACK() != null) {
      CstPairSlotBQTermList args = buildCstPairSlotBQTermList(ctx.pairSlotBqterm());
      res =  tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock.make( tom.engine.adt.cst.types.cstbqterm.Cst_BQRecordAppl.make(optionList, ctx.fsym.getText(), args) ) ;
    } else if(ctx.fsym != null && ctx.LPAREN() != null) {
      CstBQTermList args = buildCstBQTermList(ctx.composite());
      res =  tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock.make( tom.engine.adt.cst.types.cstbqterm.Cst_BQAppl.make(optionList, ctx.fsym.getText(), args) ) ;
    } else if(ctx.var != null && ctx.STAR() == null) {
      res =  tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock.make( tom.engine.adt.cst.types.cstbqterm.Cst_BQVar.make(optionList, ctx.var.getText(),  tom.engine.adt.cst.types.csttype.Cst_TypeUnknown.make() ) ) ;
    } else if(ctx.var != null && ctx.STAR() != null) {
      res =  tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock.make( tom.engine.adt.cst.types.cstbqterm.Cst_BQVarStar.make(optionList, ctx.var.getText(),  tom.engine.adt.cst.types.csttype.Cst_TypeUnknown.make() ) ) ;
    } else if (ctx.sub != null){
      res =  tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock.make((CstBQTerm)getValue(ctx.sub)) ;
    }
    setValue("exitBqcomposite",ctx,res);
  }

  /*
   * composite
   *   : fsym=tomIdentifier LPAREN (composite (COMMA composite)*)? RPAREN
   *   | LPAREN sub=composite RPAREN
   *   | var=tomIdentifier STAR?
   *   | constant
   *   | UNDERSCORE
   *   | [...]
   *   ;
   */
  public void exitComposite(TomJavaParser.CompositeContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBQTerm res = null;
    CstType type =  tom.engine.adt.cst.types.csttype.Cst_TypeUnknown.make() ;

    if(ctx.fsym != null) {
      CstBQTermList args = buildCstBQTermList(ctx.composite());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQAppl.make(optionList, ctx.fsym.getText(), args) ;
    } else if(ctx.sub != null) {
      CstOptionList optionList1 =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.LPAREN().getSymbol()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
      CstOptionList optionList2 =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.RPAREN().getSymbol()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
      CstBQTerm sub = (CstBQTerm)getValue(ctx.sub);

      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite.make(optionList,  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(optionList1, ctx.LPAREN().getText()) , tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make(sub, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(optionList2, ctx.RPAREN().getText()) , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) ) ) ) 



;
    } else if(ctx.var != null && ctx.STAR() == null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQVar.make(optionList, ctx.var.getText(), type) ;
    } else if(ctx.var != null && ctx.STAR() != null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQVarStar.make(optionList, ctx.var.getText(), type) ;
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQConstant.make(optionList, cst.getvalue()) ;
    } else if(ctx.UNDERSCORE() != null) {
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQUnderscore.make() ;
    } else {
      CstBQTermList bqList =  tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ;

      for(int i = 0; i < ctx.getChildCount(); i++) {
        if(ctx.getChild(i) instanceof TomJavaParser.CompositeContext) {
          CstBQTerm child = (CstBQTerm)getValue(ctx.getChild(i));
          bqList =  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make(child,tom_append_list_ConcCstBQTerm(bqList, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() )) ;
        } else if(ctx.getChild(i).getPayload() instanceof Token) {
          Token token = (Token) ctx.getChild(i).getPayload();
          CstOptionList tokenOptionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(token), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
          bqList =  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(tokenOptionList, token.getText()) ,tom_append_list_ConcCstBQTerm(bqList, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() )) ;
        } else {
          ParserRuleContext child = (ParserRuleContext) ctx.getChild(i);
          CstOptionList childOptionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(child.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
          bqList =  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(childOptionList, child.getText()) ,tom_append_list_ConcCstBQTerm(bqList, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() )) ;
        }
      }
      
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite.make(optionList, bqList.reverse()) ;
    }

    setValue("exitComposite",ctx,res);
  }

  /*
   * pattern
   *   : tomIdentifier AT pattern 
   *   | ANTI pattern
   *   | fsymbol explicitArgs
   *   | fsymbol implicitArgs
   *   | var=tomIdentifier STAR?
   *   | UNDERSCORE STAR?
   *   | constant (PIPE constant)*
   *   ;
   */
  public void exitPattern(TomJavaParser.PatternContext ctx) {
    CstPattern res = null;
    if(ctx.AT() != null) {
      res =  tom.engine.adt.cst.types.cstpattern.Cst_AnnotatedPattern.make((CstPattern)getValue(ctx.pattern()), ctx.tomIdentifier().getText()) ;
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
  public void exitFsymbol(TomJavaParser.FsymbolContext ctx) {
    CstSymbolList res = buildCstSymbolList(ctx.headSymbol());
    setValue("exitFsymbol",ctx,res);
  }

  /*
   * headSymbol
   *   : tomIdentifier QMARK?
   *   | tomIdentifier DQMARK?
   *   | constant
   *   ;
   */
  public void exitHeadSymbol(TomJavaParser.HeadSymbolContext ctx) {
    CstSymbol res = null;
    if(ctx.QMARK() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_Symbol.make(ctx.tomIdentifier().getText(),  tom.engine.adt.cst.types.csttheory.Cst_TheoryAU.make() ) ;
    } else if(ctx.DQMARK() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_Symbol.make(ctx.tomIdentifier().getText(),  tom.engine.adt.cst.types.csttheory.Cst_TheoryAC.make() ) ;
    } else if(ctx.tomIdentifier() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_Symbol.make(ctx.tomIdentifier().getText(),  tom.engine.adt.cst.types.csttheory.Cst_TheoryDEFAULT.make() ) ;
    } else if(ctx.constant() != null) {
      res = (CstSymbol) getValue(ctx.constant());
    } 
    setValue("exitHeadSymbol",ctx,res);
  }

  /*
   * constant
   *  : SUB? DECIMAL_LITERAL
   *  | SUB? FLOAT_LITERAL
   *  | CHAR_LITERAL
   *  | EXTENDED_CHAR_LITERAL
   *  | STRING_LITERAL
   *  ;
   */
  public void exitConstant(TomJavaParser.ConstantContext ctx) {
    CstSymbol res = null;
    if(ctx.DECIMAL_LITERAL() != null) {
      if(ctx.DECIMAL_LITERAL().getText().toLowerCase().endsWith("l")) {
        res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolLong.make(((ctx.SUB() ==null)?"":"-") +ctx.DECIMAL_LITERAL().getText()) ;
      } else {
        res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolInt.make(((ctx.SUB() ==null)?"":"-") +ctx.DECIMAL_LITERAL().getText()) ;
      }
    } else if(ctx.FLOAT_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolDouble.make(((ctx.SUB() ==null)?"":"-") +ctx.FLOAT_LITERAL().getText()) ;
    } else if(ctx.CHAR_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolChar.make(ctx.CHAR_LITERAL().getText()) ;
    } else if(ctx.EXTENDED_CHAR_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolChar.make(ctx.EXTENDED_CHAR_LITERAL().getText()) ;
    } else if(ctx.STRING_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolString.make(ctx.STRING_LITERAL().getText()) ;
    }
    setValue("exitConstant",ctx,res);
  }

  /*
   * explicitArgs
   *   : LPAREN (pattern (COMMA pattern)*)? RPAREN
   *   ;
   */
  public void exitExplicitArgs(TomJavaParser.ExplicitArgsContext ctx) {
    int n = ctx.pattern().size();
    CstPatternList res =  tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ;
    for(int i=0 ; i<n ; i++) {
      res =  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make((CstPattern)getValue(ctx.pattern(i)),tom_append_list_ConcCstPattern(res, tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() )) ;
    }
    setValue("exitExplicitArgs",ctx,res.reverse());
  }

  /*
   * implicitArgs
   *   : LSQUAREBR (tomIdentifier ASSIGN pattern (COMMA tomIdentifier ASSIGN pattern)*)? RSQUAREBR 
   *   ;
   */
  public void exitImplicitArgs(TomJavaParser.ImplicitArgsContext ctx) {
    int n = ctx.tomIdentifier().size();
    CstPairPatternList res =  tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ;
    for(int i=0 ; i<n ; i++) {
      res =  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( tom.engine.adt.cst.types.cstpairpattern.Cst_PairPattern.make(ctx.tomIdentifier(i).getText(), (CstPattern)getValue(ctx.pattern(i))) ,tom_append_list_ConcCstPairPattern(res, tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() )) ;
    }
    setValue("exitImplicitArgs",ctx,res.reverse());
  }

  /*
   * typeterm
   *   : TYPETERM type=tomIdentifier (EXTENDS supertype=tomIdentifier)? LBRACE 
   *     implement isSort? equalsTerm?
   *     RBRACE
   *   ;
   */
  public void exitTypeterm(TomJavaParser.TypetermContext ctx) {
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
         tom.engine.adt.cst.types.cstblock.Cst_TypetermConstruct.make(optionList, typeName, extendsTypeName, operatorList) 
);
  }

  /*
   * operator
   *   : OP codomain=tomIdentifier opname=tomIdentifier LPAREN slotList? RPAREN LBRACE 
   *     (isFsym | make | getSlot | getDefault)*
   *     RBRACE
   *   ;
   */
  public void exitOperator(TomJavaParser.OperatorContext ctx) {
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
         tom.engine.adt.cst.types.cstblock.Cst_OpConstruct.make(optionList, codomain, ctorName, argumentList, operatorList) 
);
  }

  /*
   * oplist
   *   : OPLIST codomain=tomIdentifier opname=tomIdentifier LPAREN domain=tomIdentifier STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyList | makeInsertList | getHead | getTail | isEmptyList)*
   *     RBRACE
   *   ;
   */
  public void exitOplist(TomJavaParser.OplistContext ctx) {
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
         tom.engine.adt.cst.types.cstblock.Cst_OpListConstruct.make(optionList, codomain, ctorName, domain, operatorList) 
);
  }

  /*
   * oparray
   *   : OPARRAY codomain=tomIdentifier opname=tomIdentifier LPAREN domain=tomIdentifier STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyArray | makeAppendArray | getElement | getSize)*
   *     RBRACE
   *   ;
   */
  public void exitOparray(TomJavaParser.OparrayContext ctx) {
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
         tom.engine.adt.cst.types.cstblock.Cst_OpArrayConstruct.make(optionList, codomain, ctorName, domain, operatorList) 
);
  }
  
  /*
   * termBlock 
   *   : LBRACE expression RBRACE
   *   ;
   */
  public void exitTermBlock(TomJavaParser.TermBlockContext ctx) {
    CstBlockList blocks =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ;
    CstBlock current = null;

    for(CstBlock block:((CstBlockList)getValue(ctx.expression())).getCollectionConcCstBlock()) {
      boolean isHostBlock = false;

      { /* unamed block */{ /* unamed block */if ( (block instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )block) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {



          current = merge(current, block);
          isHostBlock = true;
        }}}}



      if(!isHostBlock) {
        if(current != null) {
          blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
          current = null;
        }
        blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(block,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      }
    }
    if (current != null)
    {
      blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx, tom.engine.adt.cst.types.cstblock.Cst_UnamedBlock.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(otext, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , blocks.reverse()) );
  }

  /*
   * implement
   *   : IMPLEMENT LBRACE typeType RBRACE
   *   ;
   */
  public void exitImplement(TomJavaParser.ImplementContext ctx) {
    setValue("exitImplement", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Implement.make((CstBlockList)getValue(ctx.typeType())) 
);
  }

  /*
   * equalsTerm
   *   : EQUALS LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitEqualsTerm(TomJavaParser.EqualsTermContext ctx) {
    setValue("exitEquals", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Equals.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * isSort
   *   : IS_SORT LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitIsSort(TomJavaParser.IsSortContext ctx) {
    setValue("exitIsSort", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsSort.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * isFsym
   *   : IS_FSYM LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitIsFsym(TomJavaParser.IsFsymContext ctx) {
    setValue("exitIsFsym", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsFsym.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * make
   *   : MAKE LPAREN (tomIdentifier (COMMA tomIdentifier)*)? RPAREN termBlock
   *   ;
   */
  public void exitMake(TomJavaParser.MakeContext ctx) {
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    for(TomJavaParser.TomIdentifierContext e:ctx.tomIdentifier()) {
      nameList =  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(e.getText()) ,tom_append_list_ConcCstName(nameList, tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() )) ;
    }
    setValue("exitMake", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Make.make(nameList.reverse(), getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * makeEmptyList
   *   : MAKE_EMPTY LPAREN RPAREN termBlock
   *   ;
   */
  public void exitMakeEmptyList(TomJavaParser.MakeEmptyListContext ctx) {
    setValue("exitMakeEmptyList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyList.make(getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * makeEmptyArray
   *   : MAKE_EMPTY LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitMakeEmptyArray(TomJavaParser.MakeEmptyArrayContext ctx) {
    setValue("exitMakeEmptyArray", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyArray.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * makeAppendArray
   *   : MAKE_APPEND LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitMakeAppendArray(TomJavaParser.MakeAppendArrayContext ctx) {
    setValue("exitMakeAppendArray", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeAppend.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * makeInsertList
   *   : MAKE_INSERT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitMakeInsertList(TomJavaParser.MakeInsertListContext ctx) {
    setValue("exitMakeInsertList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeInsert.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * getSlot
   *   : GET_SLOT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetSlot(TomJavaParser.GetSlotContext ctx) {
    setValue("exitGetSlot", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetSlot.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * getHead
   *   : GET_HEAD LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetHead(TomJavaParser.GetHeadContext ctx) {
    setValue("exitGetHead", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetHead.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * getTail
   *   : GET_TAIL LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetTail(TomJavaParser.GetTailContext ctx) {
    setValue("exitGetTail", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetTail.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * getElement
   *   : GET_ELEMENT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetElement(TomJavaParser.GetElementContext ctx) {
    setValue("exitGetElement", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetElement.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * isEmptyList
   *   : IS_EMPTY LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitIsEmptyList(TomJavaParser.IsEmptyListContext ctx) {
    setValue("exitIsEmptyList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsEmpty.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * getSize
   *   : GET_SIZE LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetSize(TomJavaParser.GetSizeContext ctx) {
    setValue("exitGetSize", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetSize.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }

  /*
   * getDefault
   *   : GET_DEFAULT LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetDefault(TomJavaParser.GetDefaultContext ctx) {
    setValue("exitGetDefault", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetDefault.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) 
);
  }
  
  //*****************************************************GOM************************************************************
  
  /*
   * module:
   *   MODULE modulename (imports)? section
   *   ;
   */
  public void exitModule(TomJavaParser.ModuleContext ctx) {
    SectionList sections =  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make((Section)getValue(ctx.section()), tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) ;
    
    if(ctx.imports()!=null) {
      sections =  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make((Section)getValue(ctx.imports()),tom_append_list_ConcSection(sections, tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() )) ;
    }
    
    setValue("exitModule", ctx,  tom.gom.adt.gom.types.gommodule.GomModule.make((GomModuleName)getValue(ctx.modulename()), sections) );
  }
  
  /*
   * modulename:
   *   (prefixes+=gomIdentifier GOM_DOT)* moduleName=gomIdentifier
   *   ;
   */
  public void exitModulename(TomJavaParser.ModulenameContext ctx) {
    String packagePrefix = new String();
    for(TomJavaParser.GomIdentifierContext n : ctx.prefixes) {
      if(!packagePrefix.isEmpty()) {
        packagePrefix += '.';
      }
      packagePrefix += n.getText();
    }
    
    if (streamManager!=null) {
      streamManager.associatePackagePath(ctx.moduleName.getText(),packagePrefix);
    }
    
    setValue("exitModulename", ctx,  tom.gom.adt.gom.types.gommodulename.GomModuleName.make(ctx.moduleName.getText()) );
  }
  
  /*
   * imports :
   *   IMPORTS gomIdentifier*
   *   ;
   */
  public void exitImports(TomJavaParser.ImportsContext ctx) {
    ImportList imports =  tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ;
    
    for(TomJavaParser.GomIdentifierContext n : ctx.gomIdentifier()) {
      imports =  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( tom.gom.adt.gom.types.gommodulename.GomModuleName.make(n.getText()) ,tom_append_list_ConcImportedModule(imports, tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() )) ;
    }
    
    setValue("exitImports", ctx,  tom.gom.adt.gom.types.section.Imports.make(imports.reverse()) );
  }

  /*
   * section :
   *   (GOM_PUBLIC)? adtgrammar
   *   ;
   */
  public void exitSection(TomJavaParser.SectionContext ctx) {
    setValue("exitSection", ctx,  tom.gom.adt.gom.types.section.Public.make((ProductionList)getValue(ctx.adtgrammar())) );
  }
  
  /*
   * adtgrammar :
   *   syntax*
   *   ;
   */
  public void exitAdtgrammar(TomJavaParser.AdtgrammarContext ctx) {
    ProductionList productions =  tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ;
    
    for(TomJavaParser.SyntaxContext e : ctx.syntax()) {
      ProductionList syntax = (ProductionList)getValue(e);
      productions = tom_append_list_ConcProduction(syntax,tom_append_list_ConcProduction(productions, tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ));
    }
    setValue("exitAdtgrammar", ctx, productions.reverse());
  }
  
  /*
   * syntax :
   *   (GOM_ABSTRACT SYNTAX) (hookConstruct | typedecl | atomdecl)*
   *   ;
   */
  public void exitSyntax(TomJavaParser.SyntaxContext ctx) {
    ProductionList productions =  tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ;
    
    for(TomJavaParser.AtomdeclContext e : ctx.atomdecl()) {
      Production atomdecl = (Production)getValue(e);
      productions =  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make(atomdecl,tom_append_list_ConcProduction(productions, tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() )) ;
    }
    for(TomJavaParser.TypedeclContext e : ctx.typedecl()) {
      Production sortType = (Production)getValue(e);
      productions =  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make(sortType,tom_append_list_ConcProduction(productions, tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() )) ;
    }
    for(TomJavaParser.HookConstructContext e : ctx.hookConstruct()) {
      Production hook = (Production)getValue(e);
      productions =  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make(hook,tom_append_list_ConcProduction(productions, tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() )) ;
    }
    
    setValue("exitSyntax", ctx, productions);
  }
  
  /*
   * atomdecl :
   *   ATOM gomIdentifier
   *   ;
   */
  public void exitAtomdecl(TomJavaParser.AtomdeclContext ctx) {
    setValue("exitAtomdecl", ctx,  tom.gom.adt.gom.types.production.AtomDecl.make(ctx.gomIdentifier().getText()) );
  }
  
  /*
   * typedecl :
   *   typename=gomIdentifier GOM_EQUAL alternatives
   *   | ptypename=gomIdentifier BINDS (atoms+=gomIdentifier) GOM_EQUAL pattern_alternatives
   *   ;
   */
  public void enterTypedecl(TomJavaParser.TypedeclContext ctx) {
    if(ctx.typename!=null) {
      setValue2(ctx, ctx.typename.getText());
    } else {
      setValue2(ctx, ctx.ptypename.getText());
    }
  }
  
  public void exitTypedecl(TomJavaParser.TypedeclContext ctx) {
    GomType type = null;
    AtomList atoms =  tom.gom.adt.gom.types.atomlist.EmptyConcAtom.make() ;
    AlternativeList alternatives = null;
    
    if(ctx.typename!=null) {
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , ctx.typename.getText()) ;
      alternatives = (AlternativeList)getValue(ctx.alternatives());
    } else if(ctx.ptypename!=null) {
      for(TomJavaParser.GomIdentifierContext n : ctx.atoms) {
        atoms =  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make(n.getText(),tom_append_list_ConcAtom(atoms, tom.gom.adt.gom.types.atomlist.EmptyConcAtom.make() )) ;
      }
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.PatternType.make() , ctx.ptypename.getText()) ;
      alternatives = (AlternativeList)getValue(ctx.pattern_alternatives());
    }
    setValue("exitTypedecl", ctx,  tom.gom.adt.gom.types.production.SortType.make(type, atoms.reverse(), alternatives) );
  }
 
  /*
   * alternatives :
   *  ((jd+=JAVADOC ALT) | (ALT jd+=JAVADOC) | jd+=JAVADOC | {$jd.add(null);} (ALT)?) opdecl
   *  (
   *   ((jd+=JAVADOC ALT) | (ALT jd+=JAVADOC) | {$jd.add(null);} ALT) opdecl
   *  )* (GOM_SEMI)?
   *  ;
   */
  public void enterAlternatives(TomJavaParser.AlternativesContext ctx) {
    String type = (String)getValue2(ctx.getParent());
    setValue2(ctx, type);
    
    for(int i=0; i<ctx.opdecl().size(); i++) {
      setValue2(ctx.opdecl().get(i), ctx.jd.get(i));
    }
  }
  
  public void exitAlternatives(TomJavaParser.AlternativesContext ctx) {
    AlternativeList alternatives =  tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ;
    
    for(TomJavaParser.OpdeclContext e : ctx.opdecl()) {
      alternatives =  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make((Alternative)getValue(e),tom_append_list_ConcAlternative(alternatives, tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() )) ;
    }
    setValue("exitAlternatives", ctx, alternatives.reverse());
  }
  
  /*
   * pattern_alternatives :
   *   (ALT)? pattern_opdecl (ALT pattern_opdecl)* (GOM_SEMI)?
   *   ;
   */
  public void enterPattern_alternatives(TomJavaParser.Pattern_alternativesContext ctx) {
    String type = (String)getValue2(ctx.getParent());
    setValue2(ctx, type);
  }
  
  public void exitPattern_alternatives(TomJavaParser.Pattern_alternativesContext ctx) {
    AlternativeList alternatives =  tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ;
    
    for(TomJavaParser.Pattern_opdeclContext e : ctx.pattern_opdecl()) {
      alternatives =  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make((Alternative)getValue(e),tom_append_list_ConcAlternative(alternatives, tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() )) ;
    }
    setValue("exitPattern_alternatives", ctx, alternatives.reverse());
  }
  
  /*
   * opdecl :
   *   gomIdentifier fieldlist
   *   ;
   */
  public void exitOpdecl(TomJavaParser.OpdeclContext ctx) {
    Option option =  tom.gom.adt.gom.types.option.Origin.make(ctx.getStart().getLine()) ;
    Token javadoc = (Token)getValue2(ctx);
    if(javadoc!=null) {
      option =  tom.gom.adt.gom.types.option.ConsOptionList.make(option, tom.gom.adt.gom.types.option.ConsOptionList.make( tom.gom.adt.gom.types.option.Details.make(javadoc.getText()) , tom.gom.adt.gom.types.option.EmptyOptionList.make() ) ) ;
    }
    
    GomType type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , (String)getValue2(ctx.getParent())) ;
    setValue("exitOpdecl", ctx,  tom.gom.adt.gom.types.alternative.Alternative.make(ctx.gomIdentifier().getText(), (FieldList)getValue(ctx.fieldlist()), type, option) );
  }
  
  /*
   * pattern_opdecl :
   *   gomIdentifier pattern_fieldlist
   *   ;
   */
  public void exitPattern_opdecl(TomJavaParser.Pattern_opdeclContext ctx) {
    Option option =  tom.gom.adt.gom.types.option.Origin.make(ctx.getStart().getLine()) ;
    GomType type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.PatternType.make() , (String)getValue2(ctx.getParent())) ;
    setValue("exitPattern_opdecl", ctx,  tom.gom.adt.gom.types.alternative.Alternative.make(ctx.gomIdentifier().getText(), (FieldList)getValue(ctx.pattern_fieldlist()), type, option) );
  }
  
  /*
   * fieldlist :
   *   GOM_LPAREN (field (GOM_COMMA field)* )? GOM_RPAREN
   *   ;
   */
  public void exitFieldlist(TomJavaParser.FieldlistContext ctx) {
    FieldList fields =  tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ;
    
    for(TomJavaParser.FieldContext e : ctx.field()) {
      fields =  tom.gom.adt.gom.types.fieldlist.ConsConcField.make((Field)getValue(e),tom_append_list_ConcField(fields, tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() )) ;
    }
    setValue("exitFieldlist", ctx, fields.reverse());
  }
  
  /*
   * pattern_fieldlist :
   *   GOM_LPAREN (pattern_field (GOM_COMMA pattern_field)* )? GOM_RPAREN
   *   ;
   */
  public void exitPattern_fieldlist(TomJavaParser.Pattern_fieldlistContext ctx) {
    FieldList fields =  tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ;
    
    for(TomJavaParser.Pattern_fieldContext e : ctx.pattern_field()) {
      fields =  tom.gom.adt.gom.types.fieldlist.ConsConcField.make((Field)getValue(e),tom_append_list_ConcField(fields, tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() )) ;
    }
    setValue("exitPattern_fieldlist", ctx, fields.reverse());
  }
  
  /*
   * field:
   *    type=gomIdentifier GOM_STAR
   *  | LDIPLE type=ID RDIPLE GOM_STAR
   *  | name=gomIdentifier GOM_COLON type=gomIdentifier
   *  | name=gomIdentifier GOM_COLON LDIPLE type=gomIdentifier RDIPLE
   *  ;
   */
  public void exitField(TomJavaParser.FieldContext ctx) {
    GomType type = null;
    ScopeSpecifier scope =  tom.gom.adt.gom.types.scopespecifier.None.make() ;
    if(ctx.LDIPLE()==null) {
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , ctx.type.getText()) ;
    } else {
      scope =  tom.gom.adt.gom.types.scopespecifier.Refresh.make() ;
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.PatternType.make() , ctx.type.getText()) ;
    }
    
    if(ctx.GOM_STAR()!=null) {
      setValue("exitField", ctx,  tom.gom.adt.gom.types.field.StarredField.make(type, scope) );
    } else if(ctx.name!=null) {
      setValue("exitField", ctx,  tom.gom.adt.gom.types.field.NamedField.make(ctx.name.getText(), type, scope) );
    }
  }
  
  /*
   * pattern_field:
   *    type=gomIdentifier GOM_STAR
   *  | INNER name=gomIdentifier GOM_COLON type=gomIdentifier
   *  | OUTER name=gomIdentifier GOM_COLON type=gomIdentifier
   *  | NEUTRAL name=gomIdentifier GOM_COLON type=gomIdentifier
   *  | name=gomIdentifier GOM_COLON type=gomIdentifier
   *  ;
   */
  public void exitPattern_field(TomJavaParser.Pattern_fieldContext ctx) {
    GomType type = null;
    ScopeSpecifier scope =  tom.gom.adt.gom.types.scopespecifier.None.make() ;
    if(ctx.GOM_STAR()!=null) {
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.PatternType.make() , ctx.type.getText()) ;
    } else if (ctx.INNER()!=null) {
      scope =  tom.gom.adt.gom.types.scopespecifier.Inner.make() ;
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , ctx.type.getText()) ;
    } else if (ctx.OUTER()!=null) {
      scope =  tom.gom.adt.gom.types.scopespecifier.Outer.make() ;
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , ctx.type.getText()) ;
    } else if (ctx.NEUTRAL()!=null) {
      scope =  tom.gom.adt.gom.types.scopespecifier.Neutral.make() ;
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , ctx.type.getText()) ;
    } else {
      type =  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.PatternType.make() , ctx.type.getText()) ;
    }
    
    if(ctx.GOM_STAR()!=null) {
      setValue("exitPattern_field", ctx,  tom.gom.adt.gom.types.field.StarredField.make(type, scope) );
    } else if(ctx.name!=null) {
      setValue("exitPattern_field", ctx,  tom.gom.adt.gom.types.field.NamedField.make(ctx.name.getText(), type, scope) );
    }
  }
  
  /*
   * arglist:
   *  (GOM_LPAREN (gomIdentifier (GOM_COMMA gomIdentifier)* )? GOM_RPAREN)?
   *  ;
   */
  public void exitArglist(TomJavaParser.ArglistContext ctx) {
    ArgList args =  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ;
    
    for(TomJavaParser.GomIdentifierContext n : ctx.gomIdentifier()) {
      args =  tom.gom.adt.gom.types.arglist.ConsConcArg.make( tom.gom.adt.gom.types.arg.Arg.make(n.getText()) ,tom_append_list_ConcArg(args, tom.gom.adt.gom.types.arglist.EmptyConcArg.make() )) ;
    }
    setValue("exitArglist", ctx, args.reverse());
  }
  
  /*
   * hookConstruct :
   *  (hookScope)? pointCut=gomIdentifier GOM_COLON 
   *    ( hookType=RULES RULE_LPAREN RULE_RPAREN
   *      RULESTART ruleset RULEEND
   *    | hookType=GRAPHRULES RULE_LPAREN RULE_ARG ARG_COMMA (IDENTITY | FAIL) RULE_RPAREN
   *      RULESTART graphruleset RULEEND
   *    | hookType=(AC | FL | FREE) GOM_LPAREN GOM_RPAREN hookBlock
   *    | hookType=(ACU | AU) GOM_LPAREN GOM_RPAREN hookBlock
   *    | hookType=(HOOK_MAKE | HOOK_MAKE_INSERT | HOOK_MAKE_EMPTY) arglist hookBlock
   *    | hookType=HOOK_IMPORT GOM_LPAREN GOM_RPAREN hookBlock
   *    | hookType=HOOK_INTERFACE GOM_LPAREN GOM_RPAREN hookBlock
   *    | hookType=(BLOCK | MAPPING) GOM_LPAREN GOM_RPAREN hookBlock
   *    )
   *  ;
   */
  public void exitHookConstruct(TomJavaParser.HookConstructContext ctx) {
    IdKind scope =  tom.gom.adt.gom.types.idkind.KindOperator.make() ;
    
    if(ctx.hookScope()!=null) {
      scope = (IdKind)getValue(ctx.hookScope());
    }
    HookKind kind =  tom.gom.adt.gom.types.hookkind.HookKind.make(ctx.hookType.getText()) ;
    Option option =  tom.gom.adt.gom.types.option.Origin.make(ctx.getStart().getLine()) ;
    ArgList args = null;
    HookContent hook = null;
    if(ctx.RULES()!=null) {
      args =  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ;
      hook = tom.gom.adt.gom.types.hookcontent.HookRules.make((RuleList)getValue(ctx.ruleset())) ;
    } else if(ctx.GRAPHRULES()!=null) {
      if(ctx.IDENTITY()!=null) {
        args =  tom.gom.adt.gom.types.arglist.ConsConcArg.make( tom.gom.adt.gom.types.arg.Arg.make(ctx.RULE_ARG().getText()) , tom.gom.adt.gom.types.arglist.ConsConcArg.make( tom.gom.adt.gom.types.arg.Arg.make("Identity") , tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) ) ;
      } else if(ctx.FAIL()!=null) {
        args =  tom.gom.adt.gom.types.arglist.ConsConcArg.make( tom.gom.adt.gom.types.arg.Arg.make(ctx.RULE_ARG().getText()) , tom.gom.adt.gom.types.arglist.ConsConcArg.make( tom.gom.adt.gom.types.arg.Arg.make("Fail") , tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) ) ;
      }
      hook = tom.gom.adt.gom.types.hookcontent.HookRules.make((RuleList)getValue(ctx.graphruleset())) ;
    } else if(ctx.HOOK_MAKE()!=null || ctx.HOOK_MAKE_INSERT()!=null || ctx.HOOK_MAKE_EMPTY()!=null) {
      args = (ArgList)getValue(ctx.arglist());
      hook = tom.gom.adt.gom.types.hookcontent.HookCode.make(ctx.hookBlock().getText()) ;
    } else {
      args =  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ;
      hook = tom.gom.adt.gom.types.hookcontent.HookCode.make(ctx.hookBlock().getText()) ;
    }
    setValue("exitHookConstruct", ctx,
         tom.gom.adt.gom.types.production.Hook.make(scope, ctx.pointCut.getText(), kind, args, hook, option) 
);
  }
  
  /*
   * hookScope :
   *   SORT
   *   | MODULE
   *   | OPERATOR
   *   ;
   */
  public void exitHookScope(TomJavaParser.HookScopeContext ctx) {
    if(ctx.SORT()!=null) {
      setValue("exitHookScope", ctx,  tom.gom.adt.gom.types.idkind.KindSort.make() );
    } else if(ctx.MODULE()!=null) {
      setValue("exitHookScope", ctx,  tom.gom.adt.gom.types.idkind.KindModule.make() );
    } else if(ctx.OPERATOR()!=null) {
      setValue("exitHookScope", ctx,  tom.gom.adt.gom.types.idkind.KindOperator.make() );
    }
  }
  
  //******************************************************RULE**********************************************************
  
  /*
   * ruleset:
   *   (termrule)*
   * ;
   */
  public void exitRuleset(TomJavaParser.RulesetContext ctx) {
    RuleList rules =  tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ;
    for(TomJavaParser.TermruleContext e : ctx.termrule()) {
      rules =  tom.gom.adt.rule.types.rulelist.ConsRuleList.make((Rule)getValue(e),tom_append_list_RuleList(rules, tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() )) ;
    }
    setValue("exitRuleset", ctx, rules.reverse());
  }
  
  /*
   * termrule :
   *   rule_pattern RULE_ARROW rule_term (RULE_IF condition)?
   *   ;
   */
  public void exitTermrule(TomJavaParser.TermruleContext ctx) {
    Term pattern = (Term)getValue(ctx.rule_pattern());
    Term term = (Term)getValue(ctx.rule_term());
    if(ctx.condition()==null) {
      setValue("exitTermrule", ctx,  tom.gom.adt.rule.types.rule.Rule.make(pattern, term) );
    } else {
      Condition cond = (Condition)getValue(ctx.condition());
      setValue("exitTermrule", ctx,  tom.gom.adt.rule.types.rule.ConditionalRule.make(pattern, term, cond) );
    }
  }
  
  /*
   * grahruleset:
   *   (graphrule)*
   * ;
   */
  public void exitGraphruleset(TomJavaParser.GraphrulesetContext ctx) {
    RuleList rules =  tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ;
    for(TomJavaParser.GraphruleContext e : ctx.graphrule()) {
      rules =  tom.gom.adt.rule.types.rulelist.ConsRuleList.make((Rule)getValue(e),tom_append_list_RuleList(rules, tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() )) ;
    }
    setValue("exitGraphruleset", ctx, rules.reverse());
  }
  
  /*
   * graphrule :
   *   lhs=labelledpattern RULE_ARROW rhs=labelledpattern (RULE_IF condition)?
   *   ;
   */
  public void exitGraphrule(TomJavaParser.GraphruleContext ctx) {
    Term lhs = (Term)getValue(ctx.lhs);
    Term rhs = (Term)getValue(ctx.rhs);
    if(ctx.condition()==null) {
      setValue("exitGraphrule", ctx,  tom.gom.adt.rule.types.rule.Rule.make(lhs, rhs) );
    } else {
      Condition cond = (Condition)getValue(ctx.condition());
      setValue("exitGraphrule", ctx,  tom.gom.adt.rule.types.rule.ConditionalRule.make(lhs, rhs, cond) );
    }
  }
  
  /*
   * condition :
   *   cond=andcondition (RULE_OR andcondition)*
   *   ;
   */
  public void exitCondition(TomJavaParser.ConditionContext ctx) {
    if(ctx.RULE_OR()==null) {
      setValue("exitCondition", ctx, getValue(ctx.cond));
    } else {
      Condition cond =  tom.gom.adt.rule.types.condition.EmptyCondOr.make() ;
      for(TomJavaParser.AndconditionContext e : ctx.andcondition()) {
        cond =  tom.gom.adt.rule.types.condition.ConsCondOr.make((Condition)getValue(e),tom_append_list_CondOr(cond, tom.gom.adt.rule.types.condition.EmptyCondOr.make() )) ;
      }
      setValue("exitCondition", ctx, cond.reverse());
    }
  }
  
  /*
   * andcondition :
   *   cond=simplecondition (RULE_AND simplecondition)*
   *   ;
   */
  public void exitAndcondition(TomJavaParser.AndconditionContext ctx) {
    if(ctx.RULE_AND()==null) {
      setValue("exitAndcondition", ctx, getValue(ctx.cond));
    } else {
      Condition cond =  tom.gom.adt.rule.types.condition.EmptyCondAnd.make() ;
      for(TomJavaParser.SimpleconditionContext e : ctx.simplecondition()) {
        cond =  tom.gom.adt.rule.types.condition.ConsCondAnd.make((Condition)getValue(e),tom_append_list_CondAnd(cond, tom.gom.adt.rule.types.condition.EmptyCondAnd.make() )) ;
      }
      setValue("exitAndcondition", ctx, cond.reverse());
    }
  }
  
  /*
   * simplecondition :
   *   lterm=rule_term (
   *     RULE_EQUALS
   *     | NOTEQUALS
   *     | LEQ
   *     | RULE_LT
   *     | GEQ
   *     | RULE_GT
   *     | MATCH_SYMBOL
   *     ) rterm=rule_term
   *   | LPAR condition RPAR
   *   ;
   */
  public void exitSimplecondition(TomJavaParser.SimpleconditionContext ctx) {
    if(ctx.condition()!=null) {
      setValue("exitSimplecondition", ctx, getValue(ctx.condition()));
    } else {
      Term lterm = (Term)getValue(ctx.lterm);
      Term rterm = (Term)getValue(ctx.rterm);
      if(ctx.RULE_EQUALS()!=null) {
        setValue("exitSimplecondition", ctx,  tom.gom.adt.rule.types.condition.CondEquals.make(lterm, rterm) );
      } else if(ctx.NOTEQUALS()!=null) {
        setValue("exitSimplecondition", ctx,  tom.gom.adt.rule.types.condition.CondNotEquals.make(lterm, rterm) );
      } else if(ctx.LEQ()!=null) {
        setValue("exitSimplecondition", ctx,  tom.gom.adt.rule.types.condition.CondLessEquals.make(lterm, rterm) );
      } else if(ctx.RULE_LT()!=null) {
        setValue("exitSimplecondition", ctx,  tom.gom.adt.rule.types.condition.CondLessThan.make(lterm, rterm) );
      } else if(ctx.GEQ()!=null) {
        setValue("exitSimplecondition", ctx,  tom.gom.adt.rule.types.condition.CondGreaterEquals.make(lterm, rterm) );
      } else if(ctx.RULE_GT()!=null) {
        setValue("exitSimplecondition", ctx,  tom.gom.adt.rule.types.condition.CondGreaterThan.make(lterm, rterm) );
      } else if(ctx.MATCH_SYMBOL()!=null) {
        setValue("exitSimplecondition", ctx,  tom.gom.adt.rule.types.condition.CondMatch.make(lterm, rterm) );
      }
    }
  }
  
  /*
   * rule_pattern:
   *   funname=RULE_ID LPAR (rule_term (RULE_COMMA rule_term)*)? RPAR
   *   | (varname=RULE_ID) RULE_AT (funname=RULE_ID) LPAR (rule_term (RULE_COMMA rule_term)*)? RPAR
   *   | RULE_UNDERSCORE RULE_STAR?
   *   | NOT rule_pattern
   * ;
   */
  public void exitRule_pattern(TomJavaParser.Rule_patternContext ctx) {
    if(ctx.funname!=null) {
      TermList args =  tom.gom.adt.rule.types.termlist.EmptyTermList.make() ;
      for(TomJavaParser.Rule_termContext e : ctx.rule_term()) {
        args =  tom.gom.adt.rule.types.termlist.ConsTermList.make((Term)getValue(e),tom_append_list_TermList(args, tom.gom.adt.rule.types.termlist.EmptyTermList.make() )) ;
      }
      
      Term appl =  tom.gom.adt.rule.types.term.Appl.make(ctx.funname.getText(), args.reverse()) ;
      if(ctx.varname==null) {
        setValue("exitRule_pattern", ctx, appl);
      } else {
        setValue("exitRule_pattern", ctx,  tom.gom.adt.rule.types.term.At.make(ctx.varname.getText(), appl) );
      }
    } else if(ctx.RULE_UNDERSCORE()!=null) {
      if(ctx.RULE_STAR()==null) {
        setValue("exitRule_pattern", ctx,  tom.gom.adt.rule.types.term.UnnamedVar.make() );
      } else {
        setValue("exitRule_pattern", ctx,  tom.gom.adt.rule.types.term.UnnamedVarStar.make() );
      }
    } else if(ctx.rule_pattern()!=null) {
      setValue("exitRule_pattern", ctx,  tom.gom.adt.rule.types.term.Anti.make((Term)getValue(ctx.rule_pattern())) );
    }
  }
  
  /*
   * rule_term:
   *   rule_pattern
   *   | variable
   *   | builtin
   * ;
   */
  public void exitRule_term(TomJavaParser.Rule_termContext ctx) {
    if(ctx.rule_pattern()!=null) {
      setValue("exitRule_term", ctx, getValue(ctx.rule_pattern()));
    } else if(ctx.variable()!=null) {
      setValue("exitRule_term", ctx, getValue(ctx.variable()));
    } else if(ctx.builtin()!=null) {
      setValue("exitRule_term", ctx, getValue(ctx.builtin()));
    }
  }
  
  /*
   * builtin:
   *   INTEGER
   *   | STRING
   * ;
   */
  public void exitBuiltin(TomJavaParser.BuiltinContext ctx) {
    if(ctx.INTEGER()!=null) {
      setValue("exitBuiltin", ctx,  tom.gom.adt.rule.types.term.BuiltinInt.make(Integer.parseInt(ctx.INTEGER().getText())) );
    } else if(ctx.STRING()!=null) {
      setValue("exitBuiltin", ctx,  tom.gom.adt.rule.types.term.BuiltinString.make(ctx.STRING().getText()) );
    }
  }
  
  /*
   * labelledpattern :
   *   (RULE_ID RULE_COLON)? graphpattern
   *   ;
   */
  public void exitLabelledpattern(TomJavaParser.LabelledpatternContext ctx) {
    Term pattern = (Term)getValue(ctx.graphpattern());
    if(ctx.RULE_ID()==null) {
      setValue("exitLabelledpattern", ctx, pattern);
    } else {
      setValue("exitLabelledpattern", ctx,  tom.gom.adt.rule.types.term.LabTerm.make(ctx.RULE_ID().getText(), pattern) );
    }
  }
  
  /*
   * graphpattern:
   *   constructor
   *   | variable
   *   | builtin
   *   | ref
   * ;
   */
  public void exitGraphpattern(TomJavaParser.GraphpatternContext ctx) {
    if(ctx.constructor()!=null) {
      setValue("exitGraphpattern", ctx, getValue(ctx.constructor()));
    } else if(ctx.variable()!=null) {
      setValue("exitGraphpattern", ctx, getValue(ctx.variable()));
    } else if(ctx.builtin()!=null) {
      setValue("exitGraphpattern", ctx, getValue(ctx.builtin()));
    } else if(ctx.ref()!=null) {
      setValue("exitGraphpattern", ctx, getValue(ctx.ref()));
    }
  }
  
  /*
   * variable:
   *   RULE_ID (RULE_STAR)?
   * ;
   */
  public void exitVariable(TomJavaParser.VariableContext ctx) {
    if(ctx.RULE_STAR()==null) {
      setValue("exitVariable", ctx,  tom.gom.adt.rule.types.term.Var.make(ctx.RULE_ID().getText()) );
    } else {
      setValue("exitVariable", ctx,  tom.gom.adt.rule.types.term.VarStar.make(ctx.RULE_ID().getText()) );
    }
  }
  
  /*
   * ref:
   *   AMPERSAND RULE_ID
   * ;
   */
  public void exitRef(TomJavaParser.RefContext ctx) {
    setValue("exitRef", ctx,  tom.gom.adt.rule.types.term.RefTerm.make(ctx.RULE_ID().getText()) );
  }
  
  /*
   * constructor:
   *   RULE_ID LPAR (labelledpattern (RULE_COMMA labelledpattern)*)? RPAR
   * ;
   */
  public void exitConstructor(TomJavaParser.ConstructorContext ctx) {
    TermList args =  tom.gom.adt.rule.types.termlist.EmptyTermList.make() ;
    for(TomJavaParser.LabelledpatternContext e : ctx.labelledpattern()) {
      args =  tom.gom.adt.rule.types.termlist.ConsTermList.make((Term)getValue(e),tom_append_list_TermList(args, tom.gom.adt.rule.types.termlist.EmptyTermList.make() )) ;
    }
    setValue("exitConstructor", ctx,  tom.gom.adt.rule.types.term.Appl.make(ctx.RULE_ID().getText(), args.reverse()) );
  }
  
  
  
  /*
   * End of grammar
   */

  private String getText(CstOptionList ol) {
    { /* unamed block */{ /* unamed block */if ( (ol instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )ol) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )ol) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch406_end_4=(( tom.engine.adt.cst.types.CstOptionList )ol);do {{ /* unamed block */if (!( tomMatch406_end_4.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch406_8= tomMatch406_end_4.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch406_8) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginText) ) {


        return  tomMatch406_8.gettext() ;
      }}if ( tomMatch406_end_4.isEmptyConcCstOption() ) {tomMatch406_end_4=(( tom.engine.adt.cst.types.CstOptionList )ol);} else {tomMatch406_end_4= tomMatch406_end_4.getTailConcCstOption() ;}}} while(!( (tomMatch406_end_4==(( tom.engine.adt.cst.types.CstOptionList )ol)) ));}}}}


    return "";
  }
  
  private CstBlockList buildBlockList(ParserRuleContext ctx) {
    CstBlockList blocks =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ;
    CstBlock current = null;
    
    for(int i = 0; i < ctx.getChildCount(); i++) {
      if(ctx.getChild(i).getPayload() instanceof Token) {
        if(!ctx.getChild(i).getText().equals("<EOF>")) {
          Token token = (Token) ctx.getChild(i).getPayload();
          current = merge(current, token);
        }
      } else {
        ParserRuleContext child = (ParserRuleContext) ctx.getChild(i);
        
        if(child instanceof TomJavaParser.JavaIdentifierContext) {
          current = merge(current, child.getStart());
        } else if(isTom(child)) {
          if(current != null) {
            blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
            current = null;
          }
          blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((CstBlock)getValue(child),tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
        } else {
          for(CstBlock block:((CstBlockList)getValue(child)).getCollectionConcCstBlock()) {
            boolean isHostBlock = false;
            
            { /* unamed block */{ /* unamed block */if ( (block instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )block) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {



                current = merge(current, block);
                isHostBlock = true;
              }}}}


            
            if(!isHostBlock) {
              if(current != null) {
                blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
                current = null;
              }
              blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(block,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
            }
          }
        }
      }
    }
    if (current != null)
    {
      blocks =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(current,tom_append_list_ConcCstBlock(blocks, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
    }
    
    return blocks.reverse();
  }
  
  private CstBlock merge(CstBlock hostblock, Token token)
  {
    CstBlock block = buildHostblock(token);
    return merge(hostblock, block);
  }
  
  private CstBlock merge(CstBlock firstBlock, CstBlock lastBlock)
  {
    if(firstBlock == null)
    {
      return lastBlock;
    } else {
      String merge = new String();
      CstOption ot = null;
      { /* unamed block */{ /* unamed block */if ( (firstBlock instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )firstBlock) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch408_2= (( tom.engine.adt.cst.types.CstBlock )firstBlock).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch408_2) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch408_2) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch408_2.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch408_19= tomMatch408_2.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch408_19) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) { String  tom___source= tomMatch408_19.getfileName() ;if (  tomMatch408_2.getTailConcCstOption() .isEmptyConcCstOption() ) {if ( (lastBlock instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )lastBlock) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch408_6= (( tom.engine.adt.cst.types.CstBlock )lastBlock).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch408_6) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch408_6) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch408_6.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch408_26= tomMatch408_6.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch408_26) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if ( tom___source.equals( tomMatch408_26.getfileName() ) ) {if (  tomMatch408_6.getTailConcCstOption() .isEmptyConcCstOption() ) {



          merge =  (( tom.engine.adt.cst.types.CstBlock )firstBlock).getcontent()  +  (( tom.engine.adt.cst.types.CstBlock )lastBlock).getcontent() ;
          ot =  tom.engine.adt.cst.types.cstoption.Cst_OriginTracking.make(tom___source,  tomMatch408_19.getstartLine() ,  tomMatch408_19.getstartColumn() ,  tomMatch408_26.getendLine() ,  tomMatch408_26.getendColumn() ) ;
        }}}}}}}}}}}}}}}


      return  tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(ot, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , merge) ;
    }
  }
  
  private boolean isTom(Object o) {
    return o instanceof TomJavaParser.TomDeclarationContext
        || o instanceof TomJavaParser.TomStatementContext
        || o instanceof TomJavaParser.TomTermContext;
  }

  /*
   * given a context corresponding to water token
   * search spaces and layouts in hidden channels to
   * build a HOSTBLOCK with spaces and layout
   */
  private CstBlock buildHostblock(Token token) {
    String s = token.getText();
    
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
        res =  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make((CstBQTerm)getValue(e),tom_append_list_ConcCstBQTerm(res, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() )) ;
      }
    }
    return res.reverse();
  }
  
  private CstPairSlotBQTermList buildCstPairSlotBQTermList(List<? extends ParserRuleContext> ctx) {
    CstPairSlotBQTermList res =  tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res =  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make((CstPairSlotBQTerm)getValue(e),tom_append_list_ConcCstPairSlotBQTerm(res, tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() )) ;
      }
    }
    return res.reverse();
  }

  private CstConstraintActionList buildCstConstraintActionList(List<? extends ParserRuleContext> ctx) {
    CstConstraintActionList res =  tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res =  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make((CstConstraintAction)getValue(e),tom_append_list_ConcCstConstraintAction(res, tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() )) ;
      }
    }
    return res.reverse();
  }

  private CstVisitList buildCstVisitList(List<? extends ParserRuleContext> ctx) {
    CstVisitList res =  tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res =  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make((CstVisit)getValue(e),tom_append_list_ConcCstVisit(res, tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() )) ;
      }
    }
    return res.reverse();
  }

  private CstSlotList buildCstSlotList(List<? extends ParserRuleContext> ctx) {
    CstSlotList res =  tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res =  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make((CstSlot)getValue(e),tom_append_list_ConcCstSlot(res, tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() )) ;
      }
    }
    return res.reverse();
  }

  private CstPatternList buildCstPatternList(List<? extends ParserRuleContext> ctx) {
    CstPatternList res =  tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res =  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make((CstPattern)getValue(e),tom_append_list_ConcCstPattern(res, tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() )) ;
      }
    }
    return res.reverse();
  }

  private CstTermList buildCstTermList(List<? extends ParserRuleContext> ctx) {
    CstTermList res =  tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res =  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make((CstTerm)getValue(e),tom_append_list_ConcCstTerm(res, tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() )) ;
      }
    }
    return res.reverse();
  }

  private CstSymbolList buildCstSymbolList(List<? extends ParserRuleContext> ctx) {
    CstSymbolList res =  tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ;
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res =  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make((CstSymbol)getValue(e),tom_append_list_ConcCstSymbol(res, tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() )) ;
      }
    }
    return res.reverse();
  }

  /*
   * add missing spaces/newlines between two tokens
   */
  private static String betweenToken(Token t1, Token t2) {
    String newline = System.getProperty("line.separator");
    int l = t1.getLine();
    int c = t1.getCharPositionInLine()+1;
    String res = "";
    while(l < t2.getLine()) {
      res += newline;
      l++;
      c = 1;
    }
    while(c < t2.getCharPositionInLine()) {
      res += " ";
      c++;
    }
    return res;
  }
}

