
























package tom.engine.expander;



import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



import tom.engine.exception.TomRuntimeException;



import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomsignature.types.tomsymbollist.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;



import tom.engine.adt.tominstruction.types.constraintinstructionlist.concConstraintInstruction;
import tom.engine.adt.tomslot.types.slotlist.concSlot;
import tom.engine.adt.tomsignature.types.tomvisitlist.concTomVisit;
import tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass;
import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.platform.adt.platformoption.types.PlatformOptionList;



import tom.library.sl.*;






public class ExpanderPlugin extends TomGenericPlugin {

     private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_append_list_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList l1,  tom.engine.adt.tomsignature.types.TomVisitList  l2) {     if( l1.isEmptyconcTomVisit() ) {       return l2;     } else if( l2.isEmptyconcTomVisit() ) {       return l1;     } else if(  l1.getTailconcTomVisit() .isEmptyconcTomVisit() ) {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,tom_append_list_concTomVisit( l1.getTailconcTomVisit() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_get_slice_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList  begin,  tom.engine.adt.tomsignature.types.TomVisitList  end, tom.engine.adt.tomsignature.types.TomVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomVisit()  ||  (end== tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( begin.getHeadconcTomVisit() ,( tom.engine.adt.tomsignature.types.TomVisitList )tom_get_slice_concTomVisit( begin.getTailconcTomVisit() ,end,tail)) ;   }      private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_append_list_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList l1,  tom.engine.adt.tomsignature.types.TomSymbolList  l2) {     if( l1.isEmptyconcTomSymbol() ) {       return l2;     } else if( l2.isEmptyconcTomSymbol() ) {       return l1;     } else if(  l1.getTailconcTomSymbol() .isEmptyconcTomSymbol() ) {       return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( l1.getHeadconcTomSymbol() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( l1.getHeadconcTomSymbol() ,tom_append_list_concTomSymbol( l1.getTailconcTomSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_get_slice_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList  begin,  tom.engine.adt.tomsignature.types.TomSymbolList  end, tom.engine.adt.tomsignature.types.TomSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomSymbol()  ||  (end== tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( begin.getHeadconcTomSymbol() ,( tom.engine.adt.tomsignature.types.TomSymbolList )tom_get_slice_concTomSymbol( begin.getTailconcTomSymbol() ,end,tail)) ;   }      private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_append_list_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList l1,  tom.engine.adt.tomdeclaration.types.DeclarationList  l2) {     if( l1.isEmptyconcDeclaration() ) {       return l2;     } else if( l2.isEmptyconcDeclaration() ) {       return l1;     } else if(  l1.getTailconcDeclaration() .isEmptyconcDeclaration() ) {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,l2) ;     } else {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,tom_append_list_concDeclaration( l1.getTailconcDeclaration() ,l2)) ;     }   }   private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_get_slice_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList  begin,  tom.engine.adt.tomdeclaration.types.DeclarationList  end, tom.engine.adt.tomdeclaration.types.DeclarationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcDeclaration()  ||  (end== tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( begin.getHeadconcDeclaration() ,( tom.engine.adt.tomdeclaration.types.DeclarationList )tom_get_slice_concDeclaration( begin.getTailconcDeclaration() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {     if( l1.isEmptyconcTypeOption() ) {       return l2;     } else if( l2.isEmptyconcTypeOption() ) {       return l1;     } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) ;}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}







  private static Logger logger = Logger.getLogger("tom.engine.expander.ExpanderPlugin");
  
  public static final String EXPANDED_SUFFIX = ".tfix.expanded";

  
  public static final PlatformOptionList PLATFORM_OPTIONS =
     tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("expand", "", "Expander (activated by default)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("genIntrospector", "gi", "Generate a class that implements Introspector to apply strategies on non visitable terms",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) ) 


;

  private static TomType objectType;
  private static TomType genericType;
  private static TomType methodparameterType;
  private static TomType objectArrayType;
  private static TomType intType;
  
  private static TomType basicStratType;
  private static TomType introspectorType;
  private static TomType visitfailureType;
  
  private static BQTerm introspectorVar;
  private static BQTerm objectVar;
  private static BQTerm childVar;
  private static BQTerm intVar;
  private static BQTerm objectArrayVar;
  private static String visitFuncPrefix; 
  private static boolean generateAdaCode = false;

  
  private boolean genIntrospector = false;
  private boolean generatedIntrospector = false;

  public boolean getGenIntrospector() {
    return genIntrospector;
  }

  private void setGenIntrospector(boolean genIntrospector) {
    this.genIntrospector = genIntrospector;
  }

  public boolean getGeneratedIntrospector() {
    return generatedIntrospector;
  }

  private void setGeneratedIntrospector(boolean generatedIntrospector) {
    this.generatedIntrospector = generatedIntrospector;
  }

  
  public ExpanderPlugin() {
    super("ExpanderPlugin");
  }

  public void run(Map informationTracker) {
	if(getOptionBooleanValue("aCode")) {
		generateAdaCode = true;
		visitFuncPrefix = "tom_";
		objectType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","ObjectPtr");
		genericType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","ObjectPtr");
		methodparameterType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","ObjectPtr");
		objectArrayType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","ObjectPtrArrayPtr");
		intType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"int","int");

		basicStratType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","AbstractStrategyBasic");
		introspectorType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","access Introspector'Class");
		visitfailureType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","VisitFailure");
		
		introspectorVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("intro") , introspectorType) ;
		objectVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("o") , objectType) ;
		childVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("child") , objectType) ;
		intVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("i") , intType) ;
		objectArrayVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("children") , objectArrayType) ;
	} else {
		visitFuncPrefix = "_";
		objectType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","Object");
		genericType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","T");
		methodparameterType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","<T> T");
		objectArrayType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","Object[]");
		intType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"int","int");

		basicStratType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","tom.library.sl.AbstractStrategyBasic");
		introspectorType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","tom.library.sl.Introspector");
		visitfailureType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","tom.library.sl.VisitFailure");
		
		introspectorVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("introspector") , introspectorType) ;
		objectVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("o") , objectType) ;
		childVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("child") , objectType) ;
		intVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("i") , intType) ;
		objectArrayVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("children") , objectArrayType) ;
	}
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");    
    setGenIntrospector(getOptionBooleanValue("genIntrospector"));
    try {
      
      setGeneratedIntrospector(false);
      Code expandedTerm = (Code) this.expand((Code)getWorkingTerm());
      
      TomMessage.info(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.tomExpandingPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      setWorkingTerm(expandedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName()+EXPANDED_SUFFIX, (Code)getWorkingTerm());
      }
    } catch(Exception e) {
      TomMessage.error(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS; 
  }

  private tom.library.sl.Visitable expand(tom.library.sl.Visitable subject) {
    try {
      return tom_make_TopDownIdStopOnSuccess( new Expand_once(this) ).visitLight(subject);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("ExpanderPlugin.expand: fail on " + subject);
    }
  }

  
  public static class Expand_once extends tom.library.sl.AbstractStrategyBasic {private  ExpanderPlugin  expander;public Expand_once( ExpanderPlugin  expander) {super(( new tom.library.sl.Identity() ));this.expander=expander;}public  ExpanderPlugin  getexpander() {return expander;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.Strategy) ) { tom.engine.adt.tomname.types.TomName  tom___name= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getSName() ; tom.engine.adt.tomsignature.types.TomVisitList  tom___visitList= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getVisitList() ; tom.engine.adt.tomoption.types.Option  tom___orgTrack= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() ;





        
        Declaration introspectorClass =  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ;
        if(expander.getGenIntrospector() && !expander.getGeneratedIntrospector()) { /* unamed block */
          expander.setGeneratedIntrospector(true);
          DeclarationList l =  tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ;
          

          SymbolTable symbolTable = expander.getSymbolTable();
          Collection<TomType> types = symbolTable.getUsedTypes();

          
          String funcName = "getChildCount";
          
          InstructionList instructions =  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("o==null") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("0") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ;
          for(TomType type:types) { /* unamed block */
            InstructionList instructionsForSort =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
            { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___typeName= (( tom.engine.adt.tomtype.types.TomType )type).getTomType() ;

                if(!(symbolTable.isBuiltinType(tom___typeName))) { /* unamed block */
                  BQTerm var =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("v_"+tom___typeName) , type) ;
                  TomSymbolList list = symbolTable.getSymbolFromType(type);
                  { /* unamed block */{ /* unamed block */if ( (list instanceof tom.engine.adt.tomsignature.types.TomSymbolList) ) {if ( (((( tom.engine.adt.tomsignature.types.TomSymbolList )list) instanceof tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol) || ((( tom.engine.adt.tomsignature.types.TomSymbolList )list) instanceof tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol)) ) { tom.engine.adt.tomsignature.types.TomSymbolList  tomMatch251_end_4=(( tom.engine.adt.tomsignature.types.TomSymbolList )list);do {{ /* unamed block */if (!( tomMatch251_end_4.isEmptyconcTomSymbol() )) { tom.engine.adt.tomsignature.types.TomSymbol  tomMatch251_8= tomMatch251_end_4.getHeadconcTomSymbol() ;if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tomMatch251_8) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomname.types.TomName  tom___opName= tomMatch251_8.getAstName() ; tom.engine.adt.tomsignature.types.TomSymbol  tom___symbol= tomMatch251_end_4.getHeadconcTomSymbol() ;

                      Instruction inst =  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
                      if ( TomBase.isListOperator(tom___symbol) ) { /* unamed block */
                        inst =
                           tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___opName, var) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsEmptyList.make(tom___opName, var) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("0") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("1 + getChildCount(") ) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetTail.make(tom___opName, var) ) ) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make(")") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 



;
                      } else if ( TomBase.isArrayOperator(tom___symbol) ) { /* unamed block */
                        inst =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___opName, var) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetSize.make(tom___opName, var) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
                      } else { /* unamed block */
                        inst =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___opName, var) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make(""+TomBase.getArity(tom___symbol)) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
                      } 
                      instructionsForSort = tom_append_list_concInstruction(instructionsForSort, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(inst, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                    }}if ( tomMatch251_end_4.isEmptyconcTomSymbol() ) {tomMatch251_end_4=(( tom.engine.adt.tomsignature.types.TomSymbolList )list);} else {tomMatch251_end_4= tomMatch251_end_4.getTailconcTomSymbol() ;}}} while(!( (tomMatch251_end_4==(( tom.engine.adt.tomsignature.types.TomSymbolList )list)) ));}}}}

                  instructions = tom_append_list_concInstruction(instructions, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsSort.make(type, objectVar) ,  tom.engine.adt.tominstruction.types.instruction.Let.make(var,  tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(objectVar) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructionsForSort) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                }}}}}}



          
          instructions = tom_append_list_concInstruction(instructions, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("0") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
          l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(funcName) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(objectVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , intType,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructions) ) ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ;
          
          funcName = "getChildren";
          instructions =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
          for(TomType type:types) { /* unamed block */
            InstructionList instructionsForSort =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
            
            { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___typeName= (( tom.engine.adt.tomtype.types.TomType )type).getTomType() ;

                if(!symbolTable.isBuiltinType(tom___typeName)) { /* unamed block */
                  BQTerm var =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("v_"+tom___typeName) , type) ;
                  concTomSymbol list = (concTomSymbol) symbolTable.getSymbolFromType(type);
                  for(TomSymbol symbol:list) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch253_2= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom___symbolName= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getAstName() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch253_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tom___codomain= tomMatch253_2.getCodomain() ;


                        if(TomBase.isListOperator(symbol)) { /* unamed block */
                          BQTerm emptyArray =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("new Object[]{}") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ;
                          BQTerm size =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("getChildCount(o)") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ;
                          BQTerm vVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("vv") , tom___codomain) ;
                          BQTerm sizeVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("size") , intType) ;
                          BQTerm children =
                             tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("new Object[") ) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(sizeVar) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("]") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ; Instruction return_array = 
                                   tom.engine.adt.tominstruction.types.instruction.LetRef.make(intVar,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(sizeVar,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(size) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(vVar,  tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(objectVar) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(objectArrayVar,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(children) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(vVar,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(vVar) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.WhileDo.make( tom.engine.adt.tomexpression.types.expression.LessThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(intVar) ,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(sizeVar) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.AssignArray.make(objectArrayVar, intVar,  tom.engine.adt.tomexpression.types.expression.GetHead.make(tom___symbolName,  tomMatch253_2.getDomain() .getHeadconcTomType(), vVar) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(vVar,  tom.engine.adt.tomexpression.types.expression.GetTail.make(tom___symbolName, vVar) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(intVar,  tom.engine.adt.tomexpression.types.expression.AddOne.make(intVar) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(objectArrayVar) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) ) ) ) 






















;
                          
                          Instruction return_emptyArray =  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make("return new Object[]{};") ) ) ;
                          Instruction inst =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___symbolName, var) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsEmptyList.make(tom___symbolName, var) , return_emptyArray, return_array) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
                          instructionsForSort = tom_append_list_concInstruction(instructionsForSort, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(inst, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                        } else if(TomBase.isArrayOperator(symbol)) { /* unamed block */
                          
                          BQTerm emptyArray =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("new Object[]{}") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ;
                          BQTerm children =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("new Object[getChildCount(o)]") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ;
                          Instruction inst = 
                             tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___symbolName, var) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsEmptyArray.make(tom___symbolName, var,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Integer.make(0) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make(emptyArray) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(intVar,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(objectArrayVar,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(children) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.WhileDo.make( tom.engine.adt.tomexpression.types.expression.LessThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(intVar) ,  tom.engine.adt.tomexpression.types.expression.GetSize.make(tom___symbolName, var) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.AssignArray.make(objectArrayVar, intVar,  tom.engine.adt.tomexpression.types.expression.GetElement.make(tom___symbolName, tom___codomain, var, intVar) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(intVar,  tom.engine.adt.tomexpression.types.expression.AddOne.make(intVar) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(objectArrayVar) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 



















;
                          instructionsForSort = tom_append_list_concInstruction(instructionsForSort, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(inst, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );

                        } else { /* unamed block */
                          int arity = TomBase.getArity(symbol);
                          BQTerm composite =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("return new Object[]{") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ;
                          PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
                          for(int i=0; i< arity; i++) { /* unamed block */
                            PairNameDecl pairNameDecl = pairNameDeclList.getHeadconcPairNameDecl();
                            Declaration decl = pairNameDecl.getSlotDecl();
                            { /* unamed block */{ /* unamed block */if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration) ) {

                                
                                
                                composite =  tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("null") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) );
                                if(i < arity-1) { /* unamed block */
                                  composite =  tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make(",") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) );
                                }}}}{ /* unamed block */if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tom___SlotName= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getSlotName() ;


                                composite = tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetSlot.make(TomBase.getSlotType(symbol,tom___SlotName),  (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getAstName() , tom___SlotName.getString(), var) ) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) );
                                if(i < arity-1) { /* unamed block */
                                  composite = tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make(",") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) );
                                }}}}}


                            pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
                          }
                          composite = tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("};") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) );
                          Instruction inst =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___symbolName, var) ,  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.BQTermToCode.make(composite) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
                          instructionsForSort = tom_append_list_concInstruction(instructionsForSort, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(inst, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                        }}}}}}}



                  instructions = tom_append_list_concInstruction(instructions, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsSort.make(type, objectVar) ,  tom.engine.adt.tominstruction.types.instruction.Let.make(var,  tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(objectVar) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructionsForSort) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                }}}}}}



          
          instructions = tom_append_list_concInstruction(instructions, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("null") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
          l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(funcName) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(objectVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , objectArrayType,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructions) ) ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ;

          
          funcName = "setChildren";
          instructions =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
          for(TomType type:types) { /* unamed block */
            InstructionList instructionsForSort =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
            
            { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___typeName= (( tom.engine.adt.tomtype.types.TomType )type).getTomType() ;

                if(! symbolTable.isBuiltinType(tom___typeName)) { /* unamed block */
                  BQTerm var =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("v_"+tom___typeName) , type) ;
                  concTomSymbol list = (concTomSymbol) symbolTable.getSymbolFromType(type);
                  for (TomSymbol symbol:list) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomname.types.TomName  tom___symbolName= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getAstName() ; tom.engine.adt.tomtype.types.TomType  tom___TypesToType= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getTypesToType() ;


                        if (TomBase.isListOperator(symbol)) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tom___TypesToType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___TypesToType) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch257_1= (( tom.engine.adt.tomtype.types.TomType )tom___TypesToType).getDomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch257_1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch257_1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch257_1.isEmptyconcTomType() )) {if (  tomMatch257_1.getTailconcTomType() .isEmptyconcTomType() ) {


                              BQTerm res =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("res") ,  (( tom.engine.adt.tomtype.types.TomType )tom___TypesToType).getCodomain() ) ;
                              Instruction inst = 
                                 tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___symbolName, var) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("children.length==0") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.BuildEmptyList.make(tom___symbolName) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(intVar,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("children.length") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(res,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildEmptyList.make(tom___symbolName) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.WhileDo.make( tom.engine.adt.tomexpression.types.expression.GreaterThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(intVar) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(intVar,  tom.engine.adt.tomexpression.types.expression.SubstractOne.make(intVar) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(res,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildConsList.make(tom___symbolName,  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("("+symbolTable.builtinToWrapper( tomMatch257_1.getHeadconcTomType() .getTomType())+")children[i]") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , res) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(res) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 
















;
                              instructionsForSort = tom_append_list_concInstruction(instructionsForSort, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(inst, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                            }}}}}}}}

 else if(TomBase.isArrayOperator(symbol)) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tom___TypesToType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___TypesToType) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch258_1= (( tom.engine.adt.tomtype.types.TomType )tom___TypesToType).getDomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch258_1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch258_1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch258_1.isEmptyconcTomType() )) {if (  tomMatch258_1.getTailconcTomType() .isEmptyconcTomType() ) {


                              BQTerm res =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("res") ,  (( tom.engine.adt.tomtype.types.TomType )tom___TypesToType).getCodomain() ) ;
                              Instruction inst = 
                                 tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___symbolName, var) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("children.length==0") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.BuildEmptyArray.make(tom___symbolName,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Integer.make(0) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(intVar,  tom.engine.adt.tomexpression.types.expression.GetSize.make(tom___symbolName, var) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(res,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildEmptyArray.make(tom___symbolName,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetSize.make(tom___symbolName, var) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.WhileDo.make( tom.engine.adt.tomexpression.types.expression.GreaterThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(intVar) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(intVar,  tom.engine.adt.tomexpression.types.expression.SubstractOne.make(intVar) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(res,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildConsArray.make(tom___symbolName,  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("("+symbolTable.builtinToWrapper( tomMatch258_1.getHeadconcTomType() .getTomType())+")children[i]") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , res) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(res) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 

















;
                              instructionsForSort = tom_append_list_concInstruction(instructionsForSort, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(inst, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                            }}}}}}}}


 else { /* unamed block */
                          int arity = TomBase.getArity(symbol);
                          BQTermList slots =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
                          PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
                          for(int i=0; i<arity; i++) { /* unamed block */
                            PairNameDecl pairNameDecl = pairNameDeclList.getHeadconcPairNameDecl();
                            Declaration decl = pairNameDecl.getSlotDecl();
                            TomType slotType = TomBase.getSlotType(symbol,TomBase.getSlotName(symbol,i));
                            String slotTypeName = slotType.getTomType();
                            
                            if(symbolTable.isBuiltinType(slotTypeName)) { /* unamed block */
                              slots = tom_append_list_concBQTerm(slots, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("("+symbolTable.builtinToWrapper(slotTypeName)+")children["+i+"]") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
                            } else { /* unamed block */
                              slots = tom_append_list_concBQTerm(slots, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(slotType,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("children["+i+"]") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
                            }
                            pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
                          }
                          Instruction inst =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___symbolName, var) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.BuildTerm.make(tom___symbolName, slots, "default") ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
                          instructionsForSort = tom_append_list_concInstruction(instructionsForSort, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(inst, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                        }}}}}}




                  instructions = tom_append_list_concInstruction(instructions, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsSort.make(type, objectVar) ,  tom.engine.adt.tominstruction.types.instruction.Let.make(var,  tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(objectVar) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructionsForSort) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
                }}}}}}



          
          instructions = tom_append_list_concInstruction(instructions, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(objectVar) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
          l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(funcName) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(objectVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(objectArrayVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , objectType,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructions) ) ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ;

          
          funcName = "getChildAt";
          l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(funcName) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(objectVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(intVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , objectType,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("getChildren(o)[i]") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ;

          
          funcName = "setChildAt";
          String code = "\n            Object[] newChildren = getChildren(o);\n            newChildren[i] = child;\n            return setChildren(o, newChildren);\n          "



;
          l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(funcName) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(objectVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(intVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(childVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) , objectType,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make(code) ) ) ) ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ;
          introspectorClass =  tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass.make( tom.engine.adt.tomname.types.tomname.Name.make("LocalIntrospector") ,  tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl.make(l) ) ;
        }

        
        tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm methodParameterBQT; 
        TomType strategyType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined", "access " + tom___name.getString());
        BQTerm strategyVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("str") , strategyType) ; 
        DeclarationList l =  tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ; 
        HashMap<TomType,String> dispatchInfo = new HashMap<TomType,String>(); 
        { /* unamed block */{ /* unamed block */if ( (tom___visitList instanceof tom.engine.adt.tomsignature.types.TomVisitList) ) {if ( (((( tom.engine.adt.tomsignature.types.TomVisitList )tom___visitList) instanceof tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit) || ((( tom.engine.adt.tomsignature.types.TomVisitList )tom___visitList) instanceof tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit)) ) { tom.engine.adt.tomsignature.types.TomVisitList  tomMatch259_end_4=(( tom.engine.adt.tomsignature.types.TomVisitList )tom___visitList);do {{ /* unamed block */if (!( tomMatch259_end_4.isEmptyconcTomVisit() )) { tom.engine.adt.tomsignature.types.TomVisit  tomMatch259_10= tomMatch259_end_4.getHeadconcTomVisit() ;if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tomMatch259_10) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) { tom.engine.adt.tomtype.types.TomType  tomMatch259_7= tomMatch259_10.getVNode() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch259_7) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tom___vType=tomMatch259_7;

            BQTerm arg;
            if(generateAdaCode) { /* unamed block */
              arg =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("tom_arg") , tom___vType) ;
            } else { /* unamed block */
              arg =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("tom__arg") , tom___vType) ;
            }
            String funcName = "visit_" +  tomMatch259_7.getTomType() ; 
            BQTermList subjectListAST;
            if(generateAdaCode) { /* unamed block */
              subjectListAST =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(strategyVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ;
            } else { /* unamed block */
              subjectListAST =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ;
            }
            
            
            Instruction returnStatement =  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make(visitFuncPrefix+funcName) , tom___vType, subjectListAST) ) ;
            Instruction matchStatement =  tom.engine.adt.tominstruction.types.instruction.Match.make( tomMatch259_10.getAstConstraintInstructionList() ,  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ) ;
            InstructionList instructions =  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(matchStatement, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(returnStatement, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ;
            l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(funcName) , subjectListAST, tom___vType, visitfailureType,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructions) ) ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ;
            dispatchInfo.put(tom___vType,funcName);
          }}}if ( tomMatch259_end_4.isEmptyconcTomVisit() ) {tomMatch259_end_4=(( tom.engine.adt.tomsignature.types.TomVisitList )tom___visitList);} else {tomMatch259_end_4= tomMatch259_end_4.getTailconcTomVisit() ;}}} while(!( (tomMatch259_end_4==(( tom.engine.adt.tomsignature.types.TomVisitList )tom___visitList)) ));}}}}


        
        BQTerm vVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("v") , genericType) ;
        InstructionList ifList =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ; 
        Expression testEnvNotNull = null;
        
        for(TomType type:dispatchInfo.keySet()) { /* unamed block */
          BQTermList funcArg;
          if(generateAdaCode) { /* unamed block */
            funcArg =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(strategyVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(vVar) ) ) , tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ; 
          } else { /* unamed block */
            funcArg =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(vVar) ) ) , tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ;
          }
          Instruction returnStatement =  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(genericType,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make(dispatchInfo.get(type)) , type, funcArg) ) ) ) ) ;
          Instruction ifInstr =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsSort.make(type, vVar) , returnStatement,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
          ifList = tom_append_list_concInstruction(ifList, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(ifInstr, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
          
          BQTerm arg =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("arg") , type) ;
          BQTerm environmentVar;
          Instruction return1, return2;
          if(generateAdaCode) { /* unamed block */
            environmentVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("str.env") ,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) ;
            return1 =  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression.make( tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make("visit(str.any, str.env, intro)") ) ) ) ) ) ) ;
            return2 =  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression.make( tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make("visitLight(str.any, ObjectPtr(arg),intro)") ) ) ) ) ) ) ;
          } else { /* unamed block */
            environmentVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___orgTrack, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("environment") ,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) ;
            return1 =  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression.make( tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make("any.visit(environment,introspector)") ) ) ) ) ) ) ;
            return2 =  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("any.visitLight(arg,introspector)") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ;
          }

          testEnvNotNull =
             tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualTerm.make(expander.getStreamManager().getSymbolTable().getBooleanType(),  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Bottom.make( tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , "Object",  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ) ) , TomBase.convertFromBQVarToVar(environmentVar)) ) 
;
          Instruction ifThenElse =  tom.engine.adt.tominstruction.types.instruction.If.make(testEnvNotNull, return1, return2) ;
          
          if(generateAdaCode) { /* unamed block */
            methodParameterBQT =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(strategyVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ;
          } else { /* unamed block */
            methodParameterBQT =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ;
          }

          l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(visitFuncPrefix+dispatchInfo.get(type)) , methodParameterBQT, type, visitfailureType, ifThenElse) ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) 




;
        }
        if(generateAdaCode) { /* unamed block */
          ifList = tom_append_list_concInstruction(ifList, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(testEnvNotNull,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(genericType,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("visit(str.any, str.env, intro)") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(genericType,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("visitLight(str.any, v,intro)") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )


;
        } else { /* unamed block */
          ifList = tom_append_list_concInstruction(ifList, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(testEnvNotNull,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(genericType,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("any.visit(environment,introspector)") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("any.visitLight(v,introspector)") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )


;
        }

        if(generateAdaCode) { /* unamed block */
          methodParameterBQT =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(strategyVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(vVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ;
        } else { /* unamed block */
          methodParameterBQT =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(vVar, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(introspectorVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ;
        }

        Declaration visitLightDeclaration =  tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make("visitLight") , methodParameterBQT, methodparameterType, visitfailureType,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(ifList) ) 




;
        if(generateAdaCode) { /* unamed block */
          l = tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(visitLightDeclaration, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) ); 
        } else { /* unamed block */
          l =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(visitLightDeclaration,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ;
        }
        
        l= tom_append_list_concDeclaration( (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getHooks() ,tom_append_list_concDeclaration(l, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ));
        return (Declaration) expander.expand( tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl.make( tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(introspectorClass, tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.Class.make(tom___name, basicStratType,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExtendsTerm() ,  tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl.make(l) ) , tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) ) ) );
      }}}}return _visit_Declaration(tom__arg,introspector);}}

 

}
