
























package tom.engine.parser.tomjava;



import java.util.logging.Logger;
import java.util.*;



import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.*;


import tom.engine.adt.cst.types.*;



import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;



import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;











public class CstBuilder extends TomJavaParserBaseListener {
     private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_AndConstraint() ) {       return l2;     } else if( l2.isEmptyCst_AndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {       if(  l1.getTailCst_AndConstraint() .isEmptyCst_AndConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,tom_append_list_Cst_AndConstraint( l1.getTailCst_AndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_AndConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getHeadCst_AndConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_AndConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getTailCst_AndConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_OrConstraint() ) {       return l2;     } else if( l2.isEmptyCst_OrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {       if(  l1.getTailCst_OrConstraint() .isEmptyCst_OrConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,tom_append_list_Cst_OrConstraint( l1.getTailCst_OrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_OrConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getHeadCst_OrConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_OrConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getTailCst_OrConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSlotList  tom_append_list_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList l1,  tom.engine.adt.cst.types.CstSlotList  l2) {     if( l1.isEmptyConcCstSlot() ) {       return l2;     } else if( l2.isEmptyConcCstSlot() ) {       return l1;     } else if(  l1.getTailConcCstSlot() .isEmptyConcCstSlot() ) {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,tom_append_list_ConcCstSlot( l1.getTailConcCstSlot() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSlotList  tom_get_slice_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList  begin,  tom.engine.adt.cst.types.CstSlotList  end, tom.engine.adt.cst.types.CstSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSlot()  ||  (end== tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( begin.getHeadConcCstSlot() ,( tom.engine.adt.cst.types.CstSlotList )tom_get_slice_ConcCstSlot( begin.getTailConcCstSlot() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBlockList  tom_append_list_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList l1,  tom.engine.adt.cst.types.CstBlockList  l2) {     if( l1.isEmptyConcCstBlock() ) {       return l2;     } else if( l2.isEmptyConcCstBlock() ) {       return l1;     } else if(  l1.getTailConcCstBlock() .isEmptyConcCstBlock() ) {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,tom_append_list_ConcCstBlock( l1.getTailConcCstBlock() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBlockList  tom_get_slice_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList  begin,  tom.engine.adt.cst.types.CstBlockList  end, tom.engine.adt.cst.types.CstBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBlock()  ||  (end== tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( begin.getHeadConcCstBlock() ,( tom.engine.adt.cst.types.CstBlockList )tom_get_slice_ConcCstBlock( begin.getTailConcCstBlock() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_append_list_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList l1,  tom.engine.adt.cst.types.CstPairSlotBQTermList  l2) {     if( l1.isEmptyConcCstPairSlotBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstPairSlotBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstPairSlotBQTerm() .isEmptyConcCstPairSlotBQTerm() ) {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,tom_append_list_ConcCstPairSlotBQTerm( l1.getTailConcCstPairSlotBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_get_slice_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList  begin,  tom.engine.adt.cst.types.CstPairSlotBQTermList  end, tom.engine.adt.cst.types.CstPairSlotBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairSlotBQTerm()  ||  (end== tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( begin.getHeadConcCstPairSlotBQTerm() ,( tom.engine.adt.cst.types.CstPairSlotBQTermList )tom_get_slice_ConcCstPairSlotBQTerm( begin.getTailConcCstPairSlotBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOptionList  tom_append_list_ConcCstOption( tom.engine.adt.cst.types.CstOptionList l1,  tom.engine.adt.cst.types.CstOptionList  l2) {     if( l1.isEmptyConcCstOption() ) {       return l2;     } else if( l2.isEmptyConcCstOption() ) {       return l1;     } else if(  l1.getTailConcCstOption() .isEmptyConcCstOption() ) {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,tom_append_list_ConcCstOption( l1.getTailConcCstOption() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOptionList  tom_get_slice_ConcCstOption( tom.engine.adt.cst.types.CstOptionList  begin,  tom.engine.adt.cst.types.CstOptionList  end, tom.engine.adt.cst.types.CstOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOption()  ||  (end== tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( begin.getHeadConcCstOption() ,( tom.engine.adt.cst.types.CstOptionList )tom_get_slice_ConcCstOption( begin.getTailConcCstOption() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPatternList  tom_append_list_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList l1,  tom.engine.adt.cst.types.CstPatternList  l2) {     if( l1.isEmptyConcCstPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPattern() ) {       return l1;     } else if(  l1.getTailConcCstPattern() .isEmptyConcCstPattern() ) {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,tom_append_list_ConcCstPattern( l1.getTailConcCstPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPatternList  tom_get_slice_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList  begin,  tom.engine.adt.cst.types.CstPatternList  end, tom.engine.adt.cst.types.CstPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPattern()  ||  (end== tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( begin.getHeadConcCstPattern() ,( tom.engine.adt.cst.types.CstPatternList )tom_get_slice_ConcCstPattern( begin.getTailConcCstPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstNameList  tom_append_list_ConcCstName( tom.engine.adt.cst.types.CstNameList l1,  tom.engine.adt.cst.types.CstNameList  l2) {     if( l1.isEmptyConcCstName() ) {       return l2;     } else if( l2.isEmptyConcCstName() ) {       return l1;     } else if(  l1.getTailConcCstName() .isEmptyConcCstName() ) {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,tom_append_list_ConcCstName( l1.getTailConcCstName() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstNameList  tom_get_slice_ConcCstName( tom.engine.adt.cst.types.CstNameList  begin,  tom.engine.adt.cst.types.CstNameList  end, tom.engine.adt.cst.types.CstNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstName()  ||  (end== tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( begin.getHeadConcCstName() ,( tom.engine.adt.cst.types.CstNameList )tom_get_slice_ConcCstName( begin.getTailConcCstName() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOperatorList  tom_append_list_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList l1,  tom.engine.adt.cst.types.CstOperatorList  l2) {     if( l1.isEmptyConcCstOperator() ) {       return l2;     } else if( l2.isEmptyConcCstOperator() ) {       return l1;     } else if(  l1.getTailConcCstOperator() .isEmptyConcCstOperator() ) {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,tom_append_list_ConcCstOperator( l1.getTailConcCstOperator() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOperatorList  tom_get_slice_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList  begin,  tom.engine.adt.cst.types.CstOperatorList  end, tom.engine.adt.cst.types.CstOperatorList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOperator()  ||  (end== tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( begin.getHeadConcCstOperator() ,( tom.engine.adt.cst.types.CstOperatorList )tom_get_slice_ConcCstOperator( begin.getTailConcCstOperator() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstTermList  tom_append_list_ConcCstTerm( tom.engine.adt.cst.types.CstTermList l1,  tom.engine.adt.cst.types.CstTermList  l2) {     if( l1.isEmptyConcCstTerm() ) {       return l2;     } else if( l2.isEmptyConcCstTerm() ) {       return l1;     } else if(  l1.getTailConcCstTerm() .isEmptyConcCstTerm() ) {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( l1.getHeadConcCstTerm() ,tom_append_list_ConcCstTerm( l1.getTailConcCstTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstTermList  tom_get_slice_ConcCstTerm( tom.engine.adt.cst.types.CstTermList  begin,  tom.engine.adt.cst.types.CstTermList  end, tom.engine.adt.cst.types.CstTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstTerm()  ||  (end== tom.engine.adt.cst.types.csttermlist.EmptyConcCstTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.csttermlist.ConsConcCstTerm.make( begin.getHeadConcCstTerm() ,( tom.engine.adt.cst.types.CstTermList )tom_get_slice_ConcCstTerm( begin.getTailConcCstTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBQTermList  tom_append_list_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList l1,  tom.engine.adt.cst.types.CstBQTermList  l2) {     if( l1.isEmptyConcCstBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstBQTerm() .isEmptyConcCstBQTerm() ) {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,tom_append_list_ConcCstBQTerm( l1.getTailConcCstBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBQTermList  tom_get_slice_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList  begin,  tom.engine.adt.cst.types.CstBQTermList  end, tom.engine.adt.cst.types.CstBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBQTerm()  ||  (end== tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( begin.getHeadConcCstBQTerm() ,( tom.engine.adt.cst.types.CstBQTermList )tom_get_slice_ConcCstBQTerm( begin.getTailConcCstBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_append_list_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList l1,  tom.engine.adt.cst.types.CstConstraintActionList  l2) {     if( l1.isEmptyConcCstConstraintAction() ) {       return l2;     } else if( l2.isEmptyConcCstConstraintAction() ) {       return l1;     } else if(  l1.getTailConcCstConstraintAction() .isEmptyConcCstConstraintAction() ) {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,tom_append_list_ConcCstConstraintAction( l1.getTailConcCstConstraintAction() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_get_slice_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList  begin,  tom.engine.adt.cst.types.CstConstraintActionList  end, tom.engine.adt.cst.types.CstConstraintActionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstConstraintAction()  ||  (end== tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( begin.getHeadConcCstConstraintAction() ,( tom.engine.adt.cst.types.CstConstraintActionList )tom_get_slice_ConcCstConstraintAction( begin.getTailConcCstConstraintAction() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstVisitList  tom_append_list_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList l1,  tom.engine.adt.cst.types.CstVisitList  l2) {     if( l1.isEmptyConcCstVisit() ) {       return l2;     } else if( l2.isEmptyConcCstVisit() ) {       return l1;     } else if(  l1.getTailConcCstVisit() .isEmptyConcCstVisit() ) {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( l1.getHeadConcCstVisit() ,tom_append_list_ConcCstVisit( l1.getTailConcCstVisit() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstVisitList  tom_get_slice_ConcCstVisit( tom.engine.adt.cst.types.CstVisitList  begin,  tom.engine.adt.cst.types.CstVisitList  end, tom.engine.adt.cst.types.CstVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstVisit()  ||  (end== tom.engine.adt.cst.types.cstvisitlist.EmptyConcCstVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstvisitlist.ConsConcCstVisit.make( begin.getHeadConcCstVisit() ,( tom.engine.adt.cst.types.CstVisitList )tom_get_slice_ConcCstVisit( begin.getTailConcCstVisit() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairPatternList  tom_append_list_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList l1,  tom.engine.adt.cst.types.CstPairPatternList  l2) {     if( l1.isEmptyConcCstPairPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPairPattern() ) {       return l1;     } else if(  l1.getTailConcCstPairPattern() .isEmptyConcCstPairPattern() ) {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( l1.getHeadConcCstPairPattern() ,tom_append_list_ConcCstPairPattern( l1.getTailConcCstPairPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairPatternList  tom_get_slice_ConcCstPairPattern( tom.engine.adt.cst.types.CstPairPatternList  begin,  tom.engine.adt.cst.types.CstPairPatternList  end, tom.engine.adt.cst.types.CstPairPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairPattern()  ||  (end== tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( begin.getHeadConcCstPairPattern() ,( tom.engine.adt.cst.types.CstPairPatternList )tom_get_slice_ConcCstPairPattern( begin.getTailConcCstPairPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSymbolList  tom_append_list_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList l1,  tom.engine.adt.cst.types.CstSymbolList  l2) {     if( l1.isEmptyConcCstSymbol() ) {       return l2;     } else if( l2.isEmptyConcCstSymbol() ) {       return l1;     } else if(  l1.getTailConcCstSymbol() .isEmptyConcCstSymbol() ) {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,tom_append_list_ConcCstSymbol( l1.getTailConcCstSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSymbolList  tom_get_slice_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList  begin,  tom.engine.adt.cst.types.CstSymbolList  end, tom.engine.adt.cst.types.CstSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSymbol()  ||  (end== tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( begin.getHeadConcCstSymbol() ,( tom.engine.adt.cst.types.CstSymbolList )tom_get_slice_ConcCstSymbol( begin.getTailConcCstSymbol() ,end,tail)) ;   }   

  private String filename;
  private BufferedTokenStream tokens;
  private Set<Token> usedToken; 

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



  
  public void exitTomDeclaration(TomJavaParser.TomDeclarationContext ctx) {
    setValue("exitTomDeclaration", ctx, getValue(ctx.getChild(0)));
  }
  
  
  public void exitTomStatement(TomJavaParser.TomStatementContext ctx) {
    setValue("exitTomStatement", ctx, getValue(ctx.getChild(0)));
  }
  
  
  public void exitTomTerm(TomJavaParser.TomTermContext ctx) {
    setValue("exitTomTerm", ctx, getValue(ctx.getChild(0)));
  }

  
  public void exitMetaquote(TomJavaParser.MetaquoteContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    String code = ctx.METAQUOTE().getText();
    CstBlockList bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make(optionList, code) , tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ;

    setValue("exitMetaquote", ctx, tom.engine.adt.cst.types.cstblock.Cst_Metaquote.make(optionList, bl) );
  }

  
  public void exitMatchStatement(TomJavaParser.MatchStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBQTermList subjectList = buildCstBQTermList(ctx.bqterm());
    CstConstraintActionList constraintActionList = buildCstConstraintActionList(ctx.actionRule());
    CstBlock res =  tom.engine.adt.cst.types.cstblock.Cst_MatchConstruct.make(optionList, subjectList, constraintActionList) ;
    setValue("exitMatchStatement", ctx,res);
  }

  
  public void exitStrategyStatement(TomJavaParser.StrategyStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstName name =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) ;
    CstSlotList argumentList =  tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ;
    
    if(ctx.slotList() != null) {
      argumentList = (CstSlotList) getValue(ctx.slotList());
    }
    CstVisitList visitList = buildCstVisitList(ctx.visit());

    CstBlock res =  tom.engine.adt.cst.types.cstblock.Cst_StrategyConstruct.make(optionList, name, argumentList, (CstBQTerm)getValue(ctx.bqterm()), visitList) ;
    setValue("exitStrategy", ctx,res);
  }

  
  public void exitIncludeStatement(TomJavaParser.IncludeStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    String filename = "";
    for(int i = 2 ; i<ctx.getChildCount()-1 ; i++) {
      
      ParseTree child = ctx.getChild(i);
      filename += child.getText();
    }
    setValue("exitIncludeStatement", ctx, tom.engine.adt.cst.types.cstblock.Cst_IncludeFile.make(optionList, filename) );
  }

  
  public void exitGomStatement(TomJavaParser.GomStatementContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstBlock block = (CstBlock) getValue(ctx.unknownBlock());
    String text = getText(block.getoptionList());
    
    text = text.substring(1,text.length()-1).trim();
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    if(ctx.gomOptions() != null) {
      nameList = (CstNameList) getValue(ctx.gomOptions());
    }

    setValue("exitGomStatement", ctx, tom.engine.adt.cst.types.cstblock.Cst_GomConstruct.make(optionList, nameList, text) );
  }

  
  public void exitRuleStatement(TomJavaParser.RuleStatementContext ctx) {
    
  }
  
  
  public void exitUnknownBlock(TomJavaParser.UnknownBlockContext ctx) {
    CstBlockList bl =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ;

    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);

      if(child instanceof TomJavaParser.UnknownBlockContext) {
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((CstBlock)getValue(child),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else if(child instanceof TomJavaParser.TomBlockContext) {
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((CstBlock)getValue(child),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else {
        
        
        
        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(buildHostblock((Token)child.getPayload()),tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      }
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx, tom.engine.adt.cst.types.cstblock.Cst_UnamedBlock.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(otext, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , bl.reverse()) );
  }

  
  public void exitGomOptions(TomJavaParser.GomOptionsContext ctx) {
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    for(TerminalNode e:ctx.DMINUSID()) {
      nameList = tom_append_list_ConcCstName(nameList, tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(e.getText()) , tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) );
    }
    setValue("exitGomOptions", ctx, nameList);
  }

  
  public void exitVisit(TomJavaParser.VisitContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstConstraintActionList l = buildCstConstraintActionList(ctx.actionRule());
    CstVisit res =  tom.engine.adt.cst.types.cstvisit.Cst_VisitTerm.make( tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.tomIdentifier().getText()) , l, optionList) ;
    setValue("exitVisit", ctx,res);
  }

  
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

  
  public void exitSlotList(TomJavaParser.SlotListContext ctx) {
    CstSlotList res = buildCstSlotList(ctx.slot());
    setValue("exitSlotList", ctx,res);
  }

  
  public void exitSlot(TomJavaParser.SlotContext ctx) {
    CstSlot res = null;
    if(ctx.COLON() != null) {
      res =  tom.engine.adt.cst.types.cstslot.Cst_Slot.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.id2.getText()) ) ;
    } else {
      res =  tom.engine.adt.cst.types.cstslot.Cst_Slot.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) ,  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.id1.getText()) ) ;
    }
    setValue("exitSlot",ctx,res);
  }

  
  public void exitPatternlist(TomJavaParser.PatternlistContext ctx) {
    CstPatternList res = buildCstPatternList(ctx.pattern());
    setValue("exitPatternList", ctx,res);
  }

  
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

  
  public void exitPairSlotBqterm(TomJavaParser.PairSlotBqtermContext ctx) {
    CstPairSlotBQTerm res = null;
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstName slotName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) ;
    CstBQTerm bqterm = (CstBQTerm) getValue(ctx.bqterm());
    res =  tom.engine.adt.cst.types.cstpairslotbqterm.Cst_PairSlotBQTerm.make(optionList, slotName, bqterm) ;
    setValue("exitPairSlotBqterm",ctx,res);
  }

  
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
          bqList = tom_append_list_ConcCstBQTerm(bqList, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make(child, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
        } else if(ctx.getChild(i).getPayload() instanceof Token) {
          Token token = (Token) ctx.getChild(i).getPayload();
          CstOptionList tokenOptionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(token), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
          bqList = tom_append_list_ConcCstBQTerm(bqList, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(tokenOptionList, token.getText()) , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
        } else {
          ParserRuleContext child = (ParserRuleContext) ctx.getChild(i);
          CstOptionList childOptionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(child.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
          bqList = tom_append_list_ConcCstBQTerm(bqList, tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make(childOptionList, child.getText()) , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) );
        }
      }
      
      res =  tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite.make(optionList, bqList) ;
    }

    setValue("exitComposite",ctx,res);
  }

  
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
      
      
    }
    
    setValue("exitPattern",ctx,res);
  }

  
  public void exitFsymbol(TomJavaParser.FsymbolContext ctx) {
    CstSymbolList res = buildCstSymbolList(ctx.headSymbol());
    setValue("exitFsymbol",ctx,res);
  }

  
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

  
  public void exitConstant(TomJavaParser.ConstantContext ctx) {
    CstSymbol res = null;
    if(ctx.DECIMAL_LITERAL() != null) {
      if(ctx.DECIMAL_LITERAL().getText().toLowerCase().endsWith("l")) {
        res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolLong.make(((ctx.SUB()==null)?"":"-")+ctx.DECIMAL_LITERAL().getText()) ;
      } else {
        res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolInt.make(((ctx.SUB()==null)?"":"-")+ctx.DECIMAL_LITERAL().getText()) ;
      }
    } else if(ctx.FLOAT_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolDouble.make(((ctx.SUB()==null)?"":"-")+ctx.FLOAT_LITERAL().getText()) ;
    } else if(ctx.CHAR_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolChar.make(ctx.CHAR_LITERAL().getText()) ;
    } else if(ctx.EXTENDED_CHAR_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolChar.make(ctx.EXTENDED_CHAR_LITERAL().getText()) ;
    } else if(ctx.STRING_LITERAL() != null) {
      res =  tom.engine.adt.cst.types.cstsymbol.Cst_SymbolString.make(ctx.STRING_LITERAL().getText()) ;
    }
    setValue("exitConstant",ctx,res);
  }

  
  public void exitExplicitArgs(TomJavaParser.ExplicitArgsContext ctx) {
    int n = ctx.pattern().size();
    CstPatternList res =  tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ;
    for(int i=0 ; i<n ; i++) {
      res = tom_append_list_ConcCstPattern(res, tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make((CstPattern)getValue(ctx.pattern(i)), tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) );
    }
    setValue("exitExplicitArgs",ctx,res);
  }

  
  public void exitImplicitArgs(TomJavaParser.ImplicitArgsContext ctx) {
    int n = ctx.tomIdentifier().size();
    CstPairPatternList res =  tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ;
    for(int i=0 ; i<n ; i++) {
      res = tom_append_list_ConcCstPairPattern(res, tom.engine.adt.cst.types.cstpairpatternlist.ConsConcCstPairPattern.make( tom.engine.adt.cst.types.cstpairpattern.Cst_PairPattern.make(ctx.tomIdentifier(i).getText(), (CstPattern)getValue(ctx.pattern(i))) , tom.engine.adt.cst.types.cstpairpatternlist.EmptyConcCstPairPattern.make() ) );
    }
    setValue("exitImplicitArgs",ctx,res);
  }

  
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
         tom.engine.adt.cst.types.cstblock.Cst_TypetermConstruct.make(optionList, typeName, extendsTypeName, operatorList) );
  }

  
  public void exitOperator(TomJavaParser.OperatorContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType codomain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) ;
    CstName ctorName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.opname.getText()) ;
    
    CstSlotList argumentList =  tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ;
    if(ctx.slotList() != null) {
      argumentList = (CstSlotList) getValue(ctx.slotList());
    }
    
    CstOperatorList operatorList =  tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ;
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.make());
    operatorList = addCstOperator(operatorList, ctx.getSlot());
    operatorList = addCstOperator(operatorList, ctx.getDefault());
    setValue("exitOperator", ctx,
         tom.engine.adt.cst.types.cstblock.Cst_OpConstruct.make(optionList, codomain, ctorName, argumentList, operatorList) );
  }

  
  public void exitOplist(TomJavaParser.OplistContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType codomain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) ;
    CstName ctorName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.opname.getText()) ;
    CstType domain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.domain.getText()) ;
    
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

  
  public void exitOparray(TomJavaParser.OparrayContext ctx) {
    CstOptionList optionList =  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(extractOption(ctx.getStart()), tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) ;
    CstType codomain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.codomain.getText()) ;
    CstName ctorName =  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.opname.getText()) ;
    CstType domain =  tom.engine.adt.cst.types.csttype.Cst_Type.make(ctx.domain.getText()) ;
    
    CstOperatorList operatorList =  tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ;
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.makeEmptyArray());
    operatorList = addCstOperator(operatorList, ctx.makeAppendArray());
    operatorList = addCstOperator(operatorList, ctx.getElement());
    operatorList = addCstOperator(operatorList, ctx.getSize());
    setValue("exitOpArray", ctx,
         tom.engine.adt.cst.types.cstblock.Cst_OpArrayConstruct.make(optionList, codomain, ctorName, domain, operatorList) );
  }
  
  
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

  
  public void exitImplement(TomJavaParser.ImplementContext ctx) {
    setValue("exitImplement", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Implement.make((CstBlockList)getValue(ctx.typeType())) );
  }

  
  public void exitEqualsTerm(TomJavaParser.EqualsTermContext ctx) {
    setValue("exitEquals", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Equals.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitIsSort(TomJavaParser.IsSortContext ctx) {
    setValue("exitIsSort", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsSort.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitIsFsym(TomJavaParser.IsFsymContext ctx) {
    setValue("exitIsFsym", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsFsym.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitMake(TomJavaParser.MakeContext ctx) {
    CstNameList nameList =  tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ;
    for(TomJavaParser.TomIdentifierContext e:ctx.tomIdentifier()) {
      nameList = tom_append_list_ConcCstName(nameList, tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(e.getText()) , tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) );
    }
    setValue("exitMake", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_Make.make(nameList, getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitMakeEmptyList(TomJavaParser.MakeEmptyListContext ctx) {
    setValue("exitMakeEmptyList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyList.make(getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitMakeEmptyArray(TomJavaParser.MakeEmptyArrayContext ctx) {
    setValue("exitMakeEmptyArray", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeEmptyArray.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitMakeAppendArray(TomJavaParser.MakeAppendArrayContext ctx) {
    setValue("exitMakeAppendArray", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeAppend.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitMakeInsertList(TomJavaParser.MakeInsertListContext ctx) {
    setValue("exitMakeInsertList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_MakeInsert.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitGetSlot(TomJavaParser.GetSlotContext ctx) {
    setValue("exitGetSlot", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetSlot.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitGetHead(TomJavaParser.GetHeadContext ctx) {
    setValue("exitGetHead", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetHead.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitGetTail(TomJavaParser.GetTailContext ctx) {
    setValue("exitGetTail", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetTail.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitGetElement(TomJavaParser.GetElementContext ctx) {
    setValue("exitGetElement", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetElement.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id1.getText()) ,  tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.id2.getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitIsEmptyList(TomJavaParser.IsEmptyListContext ctx) {
    setValue("exitIsEmptyList", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_IsEmpty.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitGetSize(TomJavaParser.GetSizeContext ctx) {
    setValue("exitGetSize", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetSize.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  
  public void exitGetDefault(TomJavaParser.GetDefaultContext ctx) {
    setValue("exitGetDefault", ctx,
         tom.engine.adt.cst.types.cstoperator.Cst_GetDefault.make( tom.engine.adt.cst.types.cstname.Cst_Name.make(ctx.tomIdentifier().getText()) , getBlockListFromBlock(ctx.termBlock())) );
  }

  

  private String getText(CstOptionList ol) {
    { /* unamed block */{ /* unamed block */if ( (ol instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )ol) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )ol) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch388_end_4=(( tom.engine.adt.cst.types.CstOptionList )ol);do {{ /* unamed block */if (!( tomMatch388_end_4.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch388_8= tomMatch388_end_4.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch388_8) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginText) ) {

        return  tomMatch388_8.gettext() ;
      }}if ( tomMatch388_end_4.isEmptyConcCstOption() ) {tomMatch388_end_4=(( tom.engine.adt.cst.types.CstOptionList )ol);} else {tomMatch388_end_4= tomMatch388_end_4.getTailConcCstOption() ;}}} while(!( (tomMatch388_end_4==(( tom.engine.adt.cst.types.CstOptionList )ol)) ));}}}}

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
      { /* unamed block */{ /* unamed block */if ( (firstBlock instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )firstBlock) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch390_2= (( tom.engine.adt.cst.types.CstBlock )firstBlock).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch390_2) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch390_2) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch390_2.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch390_19= tomMatch390_2.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch390_19) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) { String  tom___source= tomMatch390_19.getfileName() ;if (  tomMatch390_2.getTailConcCstOption() .isEmptyConcCstOption() ) {if ( (lastBlock instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )lastBlock) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch390_6= (( tom.engine.adt.cst.types.CstBlock )lastBlock).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch390_6) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch390_6) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch390_6.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch390_26= tomMatch390_6.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch390_26) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if ( tom___source.equals( tomMatch390_26.getfileName() ) ) {if (  tomMatch390_6.getTailConcCstOption() .isEmptyConcCstOption() ) {


          merge =  (( tom.engine.adt.cst.types.CstBlock )firstBlock).getcontent()  +  (( tom.engine.adt.cst.types.CstBlock )lastBlock).getcontent() ;
          ot =  tom.engine.adt.cst.types.cstoption.Cst_OriginTracking.make(tom___source,  tomMatch390_19.getstartLine() ,  tomMatch390_19.getstartColumn() ,  tomMatch390_26.getendLine() ,  tomMatch390_26.getendColumn() ) ;
        }}}}}}}}}}}}}}}

      return  tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(ot, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , merge) ;
    }
  }
  
  private boolean isTom(Object o) {
    return o instanceof TomJavaParser.TomDeclarationContext
        || o instanceof TomJavaParser.TomStatementContext
        || o instanceof TomJavaParser.TomTermContext;
  }

  
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
    
    
    return  tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make(ot, tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , s) ;
  }

  
  private CstOption extractText(ParserRuleContext ctx) {
    int a = ctx.start.getStartIndex();
    int b = ctx.stop.getStopIndex();
    Interval interval = new Interval(a,b);
    
    
    String text = ctx.getStart().getInputStream().getText(interval);
    
    return  tom.engine.adt.cst.types.cstoption.Cst_OriginText.make(text) ;
  }

  private CstOption extractOption(Token t) {
    String newline = System.getProperty("line.separator");
    String text = t.getText();
    

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

