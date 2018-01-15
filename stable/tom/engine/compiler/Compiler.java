
























package tom.engine.compiler;



import tom.engine.tools.TomGenericPlugin;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.code.types.bqterm.BQVariable;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.library.sl.*;
import tom.engine.TomBase;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import java.util.*;



import tom.engine.tools.ASTFactory;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.engine.tools.Tools;
import java.util.logging.Level;
import tom.engine.TomMessage;











public class Compiler extends TomGenericPlugin {

     private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {     if( l1.isEmptyconcCode() ) {       return l2;     } else if( l2.isEmptyconcCode() ) {       return l1;     } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;     } else {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNumberList  tom_append_list_concTomNumber( tom.engine.adt.tomname.types.TomNumberList l1,  tom.engine.adt.tomname.types.TomNumberList  l2) {     if( l1.isEmptyconcTomNumber() ) {       return l2;     } else if( l2.isEmptyconcTomNumber() ) {       return l1;     } else if(  l1.getTailconcTomNumber() .isEmptyconcTomNumber() ) {       return  tom.engine.adt.tomname.types.tomnumberlist.ConsconcTomNumber.make( l1.getHeadconcTomNumber() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnumberlist.ConsconcTomNumber.make( l1.getHeadconcTomNumber() ,tom_append_list_concTomNumber( l1.getTailconcTomNumber() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNumberList  tom_get_slice_concTomNumber( tom.engine.adt.tomname.types.TomNumberList  begin,  tom.engine.adt.tomname.types.TomNumberList  end, tom.engine.adt.tomname.types.TomNumberList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomNumber()  ||  (end== tom.engine.adt.tomname.types.tomnumberlist.EmptyconcTomNumber.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnumberlist.ConsconcTomNumber.make( begin.getHeadconcTomNumber() ,( tom.engine.adt.tomname.types.TomNumberList )tom_get_slice_concTomNumber( begin.getTailconcTomNumber() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.theory.types.Theory  tom_append_list_concElementaryTheory( tom.engine.adt.theory.types.Theory l1,  tom.engine.adt.theory.types.Theory  l2) {     if( l1.isEmptyconcElementaryTheory() ) {       return l2;     } else if( l2.isEmptyconcElementaryTheory() ) {       return l1;     } else if(  l1.getTailconcElementaryTheory() .isEmptyconcElementaryTheory() ) {       return  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( l1.getHeadconcElementaryTheory() ,l2) ;     } else {       return  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( l1.getHeadconcElementaryTheory() ,tom_append_list_concElementaryTheory( l1.getTailconcElementaryTheory() ,l2)) ;     }   }   private static   tom.engine.adt.theory.types.Theory  tom_get_slice_concElementaryTheory( tom.engine.adt.theory.types.Theory  begin,  tom.engine.adt.theory.types.Theory  end, tom.engine.adt.theory.types.Theory  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcElementaryTheory()  ||  (end== tom.engine.adt.theory.types.theory.EmptyconcElementaryTheory.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.theory.types.theory.ConsconcElementaryTheory.make( begin.getHeadconcElementaryTheory() ,( tom.engine.adt.theory.types.Theory )tom_get_slice_concElementaryTheory( begin.getTailconcElementaryTheory() ,end,tail)) ;   }      private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) ;}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}








  private static final String freshVarPrefix = "_freshVar_";
  private static final String freshBeginPrefix = "_begin_";
  private static final String freshEndPrefix = "end";

  private CompilerEnvironment compilerEnvironment;

  public CompilerEnvironment getCompilerEnvironment() {
    return compilerEnvironment;
  }

  private class CompilerEnvironment {

    
    private SymbolTable symbolTable;
    private TomNumberList rootpath = null;
    
    
    private int matchNumber = 0;
    
    
    private int freshSubjectCounter = 0;
    private int freshVarCounter = 0;

    private ConstraintPropagator constraintPropagator; 
    private ConstraintGenerator constraintGenerator; 

    
    public CompilerEnvironment() {
      super();
      this.constraintPropagator = new ConstraintPropagator(Compiler.this); 
      this.constraintGenerator = new ConstraintGenerator(Compiler.this); 
    }

    public void nextMatch() {
      matchNumber++;
      rootpath =  tom.engine.adt.tomname.types.tomnumberlist.ConsconcTomNumber.make( tom.engine.adt.tomname.types.tomnumber.MatchNumber.make(matchNumber) , tom.engine.adt.tomname.types.tomnumberlist.EmptyconcTomNumber.make() ) ;
      freshSubjectCounter=0;
      freshVarCounter=0;
    }

    
    public SymbolTable getSymbolTable() {
      return this.symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
      this.symbolTable = symbolTable;
    }

    public TomNumberList getRootpath() {
      if(this.rootpath==null) {
        return  tom.engine.adt.tomname.types.tomnumberlist.EmptyconcTomNumber.make() ;
      } else {
        return this.rootpath;
      }
    }

    public int getMatchNumber() {
      return this.matchNumber;
    }

    public int genFreshSubjectCounter() {
      return this.freshSubjectCounter++;
    }

    public int genFreshVarCounter() {
      return this.freshVarCounter++;
    }

    public void setFreshSubjectCounter(int freshSubjectCounter) {
      this.freshSubjectCounter = freshSubjectCounter;
    }

    public void setFreshVarCounter(int freshVarCounter) {
      this.freshVarCounter = freshVarCounter;
    }

    public ConstraintPropagator getConstraintPropagator() {
      return this.constraintPropagator;
    }

    public ConstraintGenerator getConstraintGenerator() {
      return this.constraintGenerator;
    }

    

  } 


  
  public static final String COMPILED_SUFFIX = ".tfix.compiled";

  
  public static final PlatformOptionList PLATFORM_OPTIONS =
     tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("compile", "", "Compiler (activated by default)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) 

;

  
  public Compiler() {
    super("Compiler");
    compilerEnvironment = new CompilerEnvironment();
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    try {
      getCompilerEnvironment().setSymbolTable(getStreamManager().getSymbolTable());

      Code code = (Code)getWorkingTerm();
      code = addACFunctions(code);      

      
      Code compiledTerm = tom_make_TopDown( new CompileMatch(this) ).visitLight(code);

      
      Collection hashSet = new HashSet();
      Code renamedTerm =
        tom_make_TopDownIdStopOnSuccess( new findRenameVariable(hashSet) ).visitLight(compiledTerm);
      setWorkingTerm(renamedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + COMPILED_SUFFIX, renamedTerm);
      }
      TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          TomMessage.tomCompilationPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
    } catch (Exception e) {
      String fileName = getStreamManager().getInputFileName();
        TomMessage.error(getLogger(),
            fileName, 0,
            TomMessage.exceptionMessage, 
            fileName, "Compiler", e.getMessage());
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS; 
  }

  
  
  
  
  
  
  
  public static class CompileMatch extends tom.library.sl.AbstractStrategyBasic {private  Compiler  compiler;public CompileMatch( Compiler  compiler) {super(( new tom.library.sl.Identity() ));this.compiler=compiler;}public  Compiler  getcompiler() {return compiler;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom___constraintInstructionList= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() ;


        compiler.getCompilerEnvironment().nextMatch();
        int actionNumber = 0;
        TomList automataList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;	
        ArrayList<BQTerm> subjectList = new ArrayList<BQTerm>();
        ArrayList<TomTerm> renamedSubjects = new ArrayList<TomTerm>();
        
        
        { /* unamed block */{ /* unamed block */if ( (tom___constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tomMatch177_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList);do {{ /* unamed block */if (!( tomMatch177_end_4.isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch177_10= tomMatch177_end_4.getHeadconcConstraintInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tomMatch177_10) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {

            actionNumber++;
            try { /* unamed block */
              
              
              
              Constraint newConstraint = tom_make_TopDownIdStopOnSuccess( new renameSubjects(subjectList,renamedSubjects,compiler) ).visitLight( tomMatch177_10.getConstraint() );

              Constraint propagationResult = compiler.getCompilerEnvironment().getConstraintPropagator().performPropagations(newConstraint);
              PreGenerator preGenerator = new PreGenerator(compiler.getCompilerEnvironment().getConstraintGenerator());
              Expression preGeneratedExpr = preGenerator.performPreGenerationTreatment(propagationResult);
              Instruction matchingAutomata = compiler.getCompilerEnvironment().getConstraintGenerator().performGenerations(preGeneratedExpr,  tomMatch177_10.getAction() );
              Instruction postGenerationAutomata = PostGenerator.performPostGenerationTreatment(matchingAutomata);
              TomNumberList path = compiler.getCompilerEnvironment().getRootpath();
              TomNumberList numberList = tom_append_list_concTomNumber(path, tom.engine.adt.tomname.types.tomnumberlist.ConsconcTomNumber.make( tom.engine.adt.tomname.types.tomnumber.PatternNumber.make(actionNumber) , tom.engine.adt.tomname.types.tomnumberlist.EmptyconcTomNumber.make() ) );
              TomTerm automata =  tom.engine.adt.tomterm.types.tomterm.Automata.make( tomMatch177_10.getOptions() , newConstraint, numberList, postGenerationAutomata) ;
              automataList = tom_append_list_concTomTerm(automataList, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(automata, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) );
            } catch(Exception e) { /* unamed block */
              e.printStackTrace();
              throw new TomRuntimeException("Propagation or generation exception:" + e);
            }}}if ( tomMatch177_end_4.isEmptyconcConstraintInstruction() ) {tomMatch177_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList);} else {tomMatch177_end_4= tomMatch177_end_4.getTailconcConstraintInstruction() ;}}} while(!( (tomMatch177_end_4==(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList)) ));}}}}


                
        InstructionList astAutomataList = Compiler.automataListCompileMatchingList(automataList);
        
        return  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.CompiledMatch.make( tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(astAutomataList) ,  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ;
      }}}}return _visit_Instruction(tom__arg,introspector);}}public static class renameSubjects extends tom.library.sl.AbstractStrategyBasic {private  java.util.ArrayList  subjectList;private  java.util.ArrayList  renamedSubjects;private  Compiler  compiler;public renameSubjects( java.util.ArrayList  subjectList,  java.util.ArrayList  renamedSubjects,  Compiler  compiler) {super(( new tom.library.sl.Identity() ));this.subjectList=subjectList;this.renamedSubjects=renamedSubjects;this.compiler=compiler;}public  java.util.ArrayList  getsubjectList() {return subjectList;}public  java.util.ArrayList  getrenamedSubjects() {return renamedSubjects;}public  Compiler  getcompiler() {return compiler;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.code.types.BQTerm  tom___subject= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() ; tom.engine.adt.tomtype.types.TomType  tom___castType= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getAstType() ; tom.engine.adt.tomconstraint.types.Constraint  tom___constr=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);














        if(renamedSubjects.contains( (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ) || ( (tom___subject) instanceof BQVariable && renamedSubjects.contains(TomBase.convertFromBQVarToVar(tom___subject))) ) { /* unamed block */
          
          return tom___constr; 
        } 
        TomType freshSubjectType = compiler.getTermTypeFromTerm(tom___subject);
        

        
        if(subjectList.contains(tom___subject)) { /* unamed block */
          TomTerm renamedSubject = (TomTerm) renamedSubjects.get(subjectList.indexOf(tom___subject));

          if (freshSubjectType == compiler.getSymbolTable().TYPE_UNKNOWN) { /* unamed block */
            freshSubjectType = renamedSubject.getAstType();
          } 
          renamedSubject = renamedSubject.setAstType(freshSubjectType);

          BQTerm freshVar = compiler.getFreshVariable(freshSubjectType);

          Constraint newConstraint = tom___constr.setSubject(TomBase.convertFromVarToBQVar(renamedSubject));

          
          

          
          
          
          
          
          

          
            return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(TomBase.convertFromBQVarToVar(freshVar), tom___subject, freshSubjectType) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint.make(tom___castType, freshVar) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(renamedSubject,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(freshSubjectType,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(freshVar) ) ) , freshSubjectType) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newConstraint, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ) ) 



;
          
        }

        TomNumberList path = compiler.getCompilerEnvironment().getRootpath();
        TomName freshSubjectName  =  tom.engine.adt.tomname.types.tomname.PositionName.make(tom_append_list_concTomNumber(path, tom.engine.adt.tomname.types.tomnumberlist.ConsconcTomNumber.make( tom.engine.adt.tomname.types.tomnumber.NameNumber.make( tom.engine.adt.tomname.types.tomname.Name.make("_freshSubject_"+compiler.getCompilerEnvironment().genFreshSubjectCounter()) ) , tom.engine.adt.tomname.types.tomnumberlist.EmptyconcTomNumber.make() ) )) ;
        if (freshSubjectType == compiler.getSymbolTable().TYPE_UNKNOWN) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tom___subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch179_5= false ; tom.engine.adt.tomtype.types.TomType  tomMatch179_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch179_5= true ;tomMatch179_3=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_1= tomMatch179_3.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch179_5= true ;tomMatch179_4=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_1= tomMatch179_4.getAstType() ;}}}if (tomMatch179_5) {


              freshSubjectType = tomMatch179_1;
            }}}{ /* unamed block */if ( (tom___subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch179_21= false ; tom.engine.adt.code.types.BQTerm  tomMatch179_11= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_9= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_14= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_12= null ; tom.engine.adt.tomname.types.TomName  tomMatch179_7= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_10= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_13= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_16= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_15= null ; tom.engine.adt.code.types.BQTerm  tomMatch179_17= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_9=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_9.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_10=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_10.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_11=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_11.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_12=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_12.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_13=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_13.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_14=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_14.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_15=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_15.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_16=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_16.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {{ /* unamed block */tomMatch179_21= true ;tomMatch179_17=(( tom.engine.adt.code.types.BQTerm )tom___subject);tomMatch179_7= tomMatch179_17.getAstName() ;}}}}}}}}}}if (tomMatch179_21) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch179_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom___sv=(( tom.engine.adt.code.types.BQTerm )tom___subject);

              TomSymbol tomSymbol = compiler.getSymbolTable().getSymbolFromName( tomMatch179_7.getString() );
              if(tomSymbol != null) { /* unamed block */
                freshSubjectType = TomBase.getSymbolCodomain(tomSymbol);
              } else if(tom___sv.isFunctionCall()) { /* unamed block */
                freshSubjectType =tom___sv.getAstType();
              }}}}}}}




        TomTerm renamedVar =  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , freshSubjectName, freshSubjectType,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
        subjectList.add(tom___subject);
        renamedSubjects.add(renamedVar);
        Constraint newConstraint = tom___constr.setSubject(TomBase.convertFromVarToBQVar(renamedVar));
        BQTerm freshVar = compiler.getFreshVariable(freshSubjectType);

        
        
        
        
        
        
        
        
        
          return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(TomBase.convertFromBQVarToVar(freshVar), tom___subject, freshSubjectType) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint.make(tom___castType, freshVar) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(renamedVar,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(freshSubjectType,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(freshVar) ) ) , freshSubjectType) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newConstraint, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ) ) 



;
        

      }}}}return _visit_Constraint(tom__arg,introspector);}}



  
  private static InstructionList automataListCompileMatchingList(TomList automataList) {
    { /* unamed block */{ /* unamed block */if ( (automataList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )automataList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )automataList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if ( (( tom.engine.adt.tomterm.types.TomList )automataList).isEmptyconcTomTerm() ) {
 return  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ; }}}}{ /* unamed block */if ( (automataList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )automataList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )automataList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( (( tom.engine.adt.tomterm.types.TomList )automataList).isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch180_10= (( tom.engine.adt.tomterm.types.TomList )automataList).getHeadconcTomTerm() ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch180_10) instanceof tom.engine.adt.tomterm.types.tomterm.Automata) ) { tom.engine.adt.tomoption.types.OptionList  tom___optionList= tomMatch180_10.getOptions() ; tom.engine.adt.tomconstraint.types.Constraint  tom___constraint= tomMatch180_10.getConstraint() ; tom.engine.adt.tominstruction.types.Instruction  tom___instruction= tomMatch180_10.getInst() ;

        InstructionList newList = automataListCompileMatchingList( (( tom.engine.adt.tomterm.types.TomList )automataList).getTailconcTomTerm() );				
        
        
        { /* unamed block */{ /* unamed block */if ( (tom___optionList instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )tom___optionList) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tom___optionList) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch181_end_4=(( tom.engine.adt.tomoption.types.OptionList )tom___optionList);do {{ /* unamed block */if (!( tomMatch181_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch181_8= tomMatch181_end_4.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch181_8) instanceof tom.engine.adt.tomoption.types.option.Label) ) { tom.engine.adt.tomname.types.TomName  tomMatch181_7= tomMatch181_8.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch181_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

            
            return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.CompiledPattern.make(tom___constraint,  tom.engine.adt.tominstruction.types.instruction.NamedBlock.make( tomMatch181_7.getString() ,  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___instruction, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ,tom_append_list_concInstruction(newList, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) ;
          }}}if ( tomMatch181_end_4.isEmptyconcOption() ) {tomMatch181_end_4=(( tom.engine.adt.tomoption.types.OptionList )tom___optionList);} else {tomMatch181_end_4= tomMatch181_end_4.getTailconcOption() ;}}} while(!( (tomMatch181_end_4==(( tom.engine.adt.tomoption.types.OptionList )tom___optionList)) ));}}}}

        
        return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.CompiledPattern.make(tom___constraint, tom___instruction) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ,tom_append_list_concInstruction(newList, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) ;
      }}}}}}

    return null;
  }

  

  public SymbolTable getSymbolTable() {
    return getCompilerEnvironment().getSymbolTable();
  }

  
  public TomType getTermTypeFromName(TomName tomName) {
    String stringName = ((Name)tomName).getString();
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(stringName);    
    if(tomSymbol == null) {
      throw new TomRuntimeException("Unknown symbol:" + stringName);
    }
    return tomSymbol.getTypesToType().getCodomain();
  }


  
  public TomType getSlotType(TomName tomName, TomName slotName) {
    String stringName = ((Name)tomName).getString();
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(stringName);
    if(tomSymbol == null) {
      throw new TomRuntimeException("Unknown symbol:" + stringName);
    }
    return TomBase.getSlotType(tomSymbol,slotName);    
  } 

  public TomType getTermTypeFromTerm(TomTerm tomTerm) {    
    return TomBase.getTermType(tomTerm,getCompilerEnvironment().getSymbolTable());    
  }
 
  public TomType getTermTypeFromTerm(BQTerm tomTerm) {    
    return TomBase.getTermType(tomTerm,getCompilerEnvironment().getSymbolTable());    
  }

  public BQTerm getFreshVariable(TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+n);
    return  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , newName, type) ;
  }

  public BQTerm getFreshVariable(String name, TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+name+"_"+n);
    return  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , newName, type) ;
  }

  public BQTerm getFreshVariableStar(TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+n);
    return  tom.engine.adt.code.types.bqterm.BQVariableStar.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , newName, type) ;
  }

  public BQTerm getFreshVariableStar(String name, TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+name+"_"+n);
    return  tom.engine.adt.code.types.bqterm.BQVariableStar.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , newName, type) ;
  }

  public BQTerm getBeginVariableStar(TomType type) {
    return getFreshVariableStar(freshBeginPrefix,type);
  }

  public BQTerm getEndVariableStar(TomType type) {
    return getFreshVariableStar(freshEndPrefix,type);
  }

  private TomName buildVariableName(String name) {
    TomNumberList path = getCompilerEnvironment().getRootpath();
    TomName freshVarName =  tom.engine.adt.tomname.types.tomname.PositionName.make(tom_append_list_concTomNumber(path, tom.engine.adt.tomname.types.tomnumberlist.ConsconcTomNumber.make( tom.engine.adt.tomname.types.tomnumber.NameNumber.make( tom.engine.adt.tomname.types.tomname.Name.make(name) ) , tom.engine.adt.tomname.types.tomnumberlist.EmptyconcTomNumber.make() ) )) ;
    return freshVarName;
  }

  
  private static int findRenameVariableCounter = 0;
  public static class findRenameVariable extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  context;public findRenameVariable( java.util.Collection  context) {super(( new tom.library.sl.Identity() ));this.context=context;}public  java.util.Collection  getcontext() {return context;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {











        
        
        Collection newContext = new HashSet();
        Collection rhsContext = new HashSet();
        tom_make_TopDownCollect( new CollectLHSVars(newContext,rhsContext) ).visitLight( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getContraint() );        
        newContext.addAll(context);
        return tom_make_TopDownIdStopOnSuccess( new findRenameVariable(newContext) ).visitLight( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAutomataInst() );
      }}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch183_8= false ; tom.engine.adt.code.types.BQTerm  tomMatch183_4= null ; tom.engine.adt.tomname.types.TomName  tomMatch183_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch183_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch183_8= true ;tomMatch183_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch183_1= tomMatch183_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch183_8= true ;tomMatch183_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch183_1= tomMatch183_4.getAstName() ;}}}if (tomMatch183_8) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch183_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         if(context.contains(tomMatch183_1)) { /* unamed block */           String prefix = "tom";           return (( tom.engine.adt.code.types.BQTerm )tom__arg).setAstName( tom.engine.adt.tomname.types.tomname.Name.make(ASTFactory.makeTomVariableName(prefix, tomMatch183_1.getString() )) );         }}}}}}return _visit_BQTerm(tom__arg,introspector);}}public static class CollectLHSVars extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  bag;private  java.util.Collection  alreadyInRhs;public CollectLHSVars( java.util.Collection  bag,  java.util.Collection  alreadyInRhs) {super(( new tom.library.sl.Identity() ));this.bag=bag;this.alreadyInRhs=alreadyInRhs;}public  java.util.Collection  getbag() {return bag;}public  java.util.Collection  getalreadyInRhs() {return alreadyInRhs;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {





          
        
        Map rhsMap = TomBase.collectMultiplicity( (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() );
        alreadyInRhs.addAll(rhsMap.keySet());
        
        Map map = TomBase.collectMultiplicity( (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() );
        Collection newContext = new HashSet(map.keySet());
        for (Object o:newContext) { /* unamed block */
          if (!alreadyInRhs.contains(o)) { /* unamed block */
            bag.add(o);       
          }}
        
        throw new VisitFailure();
      }}}}return _visit_Constraint(tom__arg,introspector);}}



  

  

  private static String next_minimal_extract = 
        "\n          public boolean next_minimal_extract(int multiplicity, int total, int E[], int sol[]) {\n            int pos = total-1;\n            while(pos>=0 && (sol[pos]+multiplicity)>E[pos]) {\n              sol[pos]=0;\n              pos--;\n            }\n            if(pos<0) {\n              return false;\n            }\n            sol[pos]+=multiplicity;\n            return true;\n          }\n        "












;

  
  private Code addACFunctions(Code subject) throws VisitFailure {
    
    
    HashSet<String> bag = new HashSet<String>();
    tom_make_TopDown( new CollectACSymbols(bag) ).visitLight(subject);
    if (! bag.isEmpty()) {
      
      
      getSymbolTable().setUsedSymbolConstructor(getSymbolTable().getIntArrayOp());
      getSymbolTable().setUsedSymbolDestructor(getSymbolTable().getIntArrayOp());
    }
    CodeList l =  tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
    boolean generatedNextMinimalExtract = false;
    for(String op:bag) {

      TomSymbol opSymbol = getSymbolTable().getSymbolFromName(op);
      
      TomType opType = opSymbol.getTypesToType().getCodomain();        
      
      l =  tom.engine.adt.code.types.codelist.ConsconcCode.make( tom.engine.adt.code.types.code.DeclarationToCode.make(getPILforComputeLength(op,opType)) ,tom_append_list_concCode(l, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
      
      l =  tom.engine.adt.code.types.codelist.ConsconcCode.make( tom.engine.adt.code.types.code.DeclarationToCode.make(getPILforGetMultiplicities(op,opType)) ,tom_append_list_concCode(l, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
      
      l =  tom.engine.adt.code.types.codelist.ConsconcCode.make( tom.engine.adt.code.types.code.DeclarationToCode.make(getPILforGetTermForMultiplicity(op,opType)) ,tom_append_list_concCode(l, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
      
      if(!generatedNextMinimalExtract) {
        l =  tom.engine.adt.code.types.codelist.ConsconcCode.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make(next_minimal_extract) ) ,tom_append_list_concCode(l, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
        generatedNextMinimalExtract = true;
      }
    }
    
    l = PostGenerator.changeVarDeclarations(l);
    subject = tom_make_OnceTopDownId( new InsertDeclarations(l) ).visitLight(subject);          
    return subject;
  }

  public static class CollectACSymbols extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashSet  bag;public CollectACSymbols( java.util.HashSet  bag) {super(( new tom.library.sl.Identity() ));this.bag=bag;}public  java.util.HashSet  getbag() {return bag;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch185_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tomMatch185_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch185_1) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch185_1) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch185_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch185_15= tomMatch185_1.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch185_15) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch185_2) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch185_2) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch185_end_11=tomMatch185_2;do {{ /* unamed block */if (!( tomMatch185_end_11.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch185_18= tomMatch185_end_11.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch185_18) instanceof tom.engine.adt.tomoption.types.option.MatchingTheory) ) { tom.engine.adt.theory.types.Theory  tomMatch185_17= tomMatch185_18.getTheory() ;if ( (((( tom.engine.adt.theory.types.Theory )tomMatch185_17) instanceof tom.engine.adt.theory.types.theory.ConsconcElementaryTheory) || ((( tom.engine.adt.theory.types.Theory )tomMatch185_17) instanceof tom.engine.adt.theory.types.theory.EmptyconcElementaryTheory)) ) { tom.engine.adt.theory.types.Theory  tomMatch185_end_23=tomMatch185_17;do {{ /* unamed block */if (!( tomMatch185_end_23.isEmptyconcElementaryTheory() )) {if ( ((( tom.engine.adt.theory.types.ElementaryTheory ) tomMatch185_end_23.getHeadconcElementaryTheory() ) instanceof tom.engine.adt.theory.types.elementarytheory.AC) ) {

 
        bag.add( tomMatch185_15.getString() );
      }}if ( tomMatch185_end_23.isEmptyconcElementaryTheory() ) {tomMatch185_end_23=tomMatch185_17;} else {tomMatch185_end_23= tomMatch185_end_23.getTailconcElementaryTheory() ;}}} while(!( (tomMatch185_end_23==tomMatch185_17) ));}}}if ( tomMatch185_end_11.isEmptyconcOption() ) {tomMatch185_end_11=tomMatch185_2;} else {tomMatch185_end_11= tomMatch185_end_11.getTailconcOption() ;}}} while(!( (tomMatch185_end_11==tomMatch185_2) ));}}}}}}}}return _visit_TomTerm(tom__arg,introspector);}}public static class InsertDeclarations extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.code.types.CodeList  l;public InsertDeclarations( tom.engine.adt.code.types.CodeList  l) {super(( new tom.library.sl.Identity() ));this.l=l;}public  tom.engine.adt.code.types.CodeList  getl() {return l;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.InstructionList) ) {return ((T)visit_InstructionList((( tom.engine.adt.tominstruction.types.InstructionList )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.CodeList) ) {return ((T)visit_CodeList((( tom.engine.adt.code.types.CodeList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.CodeList  _visit_CodeList( tom.engine.adt.code.types.CodeList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.CodeList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.InstructionList  _visit_InstructionList( tom.engine.adt.tominstruction.types.InstructionList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.InstructionList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.InstructionList  visit_InstructionList( tom.engine.adt.tominstruction.types.InstructionList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.InstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tom__arg) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tom__arg) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch186_end_4=(( tom.engine.adt.tominstruction.types.InstructionList )tom__arg);do {{ /* unamed block */if (!( tomMatch186_end_4.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch186_8= tomMatch186_end_4.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch186_8) instanceof tom.engine.adt.tominstruction.types.instruction.CodeToInstruction) ) {if ( ((( tom.engine.adt.code.types.Code ) tomMatch186_8.getCode() ) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {












        
        InstructionList il =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
        { /* unamed block */{ /* unamed block */if ( (l instanceof tom.engine.adt.code.types.CodeList) ) {if ( (((( tom.engine.adt.code.types.CodeList )l) instanceof tom.engine.adt.code.types.codelist.ConsconcCode) || ((( tom.engine.adt.code.types.CodeList )l) instanceof tom.engine.adt.code.types.codelist.EmptyconcCode)) ) { tom.engine.adt.code.types.CodeList  tomMatch187_end_4=(( tom.engine.adt.code.types.CodeList )l);do {{ /* unamed block */if (!( tomMatch187_end_4.isEmptyconcCode() )) {
 il = tom_append_list_concInstruction(il, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tomMatch187_end_4.getHeadconcCode() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ); }if ( tomMatch187_end_4.isEmptyconcCode() ) {tomMatch187_end_4=(( tom.engine.adt.code.types.CodeList )l);} else {tomMatch187_end_4= tomMatch187_end_4.getTailconcCode() ;}}} while(!( (tomMatch187_end_4==(( tom.engine.adt.code.types.CodeList )l)) ));}}}}
         
        return tom_append_list_concInstruction(tom_get_slice_concInstruction((( tom.engine.adt.tominstruction.types.InstructionList )tom__arg),tomMatch186_end_4, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ),tom_append_list_concInstruction(il, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tomMatch186_end_4.getHeadconcInstruction() ,tom_append_list_concInstruction( tomMatch186_end_4.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) ));
      }}}if ( tomMatch186_end_4.isEmptyconcInstruction() ) {tomMatch186_end_4=(( tom.engine.adt.tominstruction.types.InstructionList )tom__arg);} else {tomMatch186_end_4= tomMatch186_end_4.getTailconcInstruction() ;}}} while(!( (tomMatch186_end_4==(( tom.engine.adt.tominstruction.types.InstructionList )tom__arg)) ));}}}}return _visit_InstructionList(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.CodeList  visit_CodeList( tom.engine.adt.code.types.CodeList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.CodeList) ) {if ( (((( tom.engine.adt.code.types.CodeList )tom__arg) instanceof tom.engine.adt.code.types.codelist.ConsconcCode) || ((( tom.engine.adt.code.types.CodeList )tom__arg) instanceof tom.engine.adt.code.types.codelist.EmptyconcCode)) ) { tom.engine.adt.code.types.CodeList  tomMatch188_end_4=(( tom.engine.adt.code.types.CodeList )tom__arg);do {{ /* unamed block */if (!( tomMatch188_end_4.isEmptyconcCode() )) {if ( ((( tom.engine.adt.code.types.Code ) tomMatch188_end_4.getHeadconcCode() ) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {{ /* unamed block */{ /* unamed block */if ( (l instanceof tom.engine.adt.code.types.CodeList) ) {if ( (((( tom.engine.adt.code.types.CodeList )l) instanceof tom.engine.adt.code.types.codelist.ConsconcCode) || ((( tom.engine.adt.code.types.CodeList )l) instanceof tom.engine.adt.code.types.codelist.EmptyconcCode)) ) { return tom_append_list_concCode(tom_get_slice_concCode((( tom.engine.adt.code.types.CodeList )tom__arg),tomMatch188_end_4, tom.engine.adt.code.types.codelist.EmptyconcCode.make() ),tom_append_list_concCode((( tom.engine.adt.code.types.CodeList )l), tom.engine.adt.code.types.codelist.ConsconcCode.make( tomMatch188_end_4.getHeadconcCode() ,tom_append_list_concCode( tomMatch188_end_4.getTailconcCode() , tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) )); }}}}}}if ( tomMatch188_end_4.isEmptyconcCode() ) {tomMatch188_end_4=(( tom.engine.adt.code.types.CodeList )tom__arg);} else {tomMatch188_end_4= tomMatch188_end_4.getTailconcCode() ;}}} while(!( (tomMatch188_end_4==(( tom.engine.adt.code.types.CodeList )tom__arg)) ));}}}}return _visit_CodeList(tom__arg,introspector);}}



  
  private Declaration getPILforGetMultiplicities(String opNameString, TomType opType) {
    TomType intType = getSymbolTable().getIntType();
    TomType intArrayType = getSymbolTable().getIntArrayType();
    
    TomName intArrayName =  tom.engine.adt.tomname.types.tomname.Name.make(getSymbolTable().getIntArrayOp()) ;    

    BQTerm subject =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("subject") , opType) ;
    BQTerm length = getFreshVariable("length",intType);
    BQTerm mult = getFreshVariable("mult",intArrayType);
    BQTerm oldElem =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("oldElem") , opType) ;

    TomName opName =  tom.engine.adt.tomname.types.tomname.Name.make(opNameString) ;
    
    Instruction ifList =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, subject,  tom.engine.adt.code.types.bqterm.BuildEmptyList.make(opName) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make(mult) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, subject) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(oldElem,  tom.engine.adt.tomexpression.types.expression.GetHead.make(opName, opType, subject) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.AssignArray.make(mult,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Integer.make(0) ) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(1) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(mult) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) 





;

    
    BQTerm elem =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("elem") , opType) ;
    BQTerm counter =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("counter") , getSymbolTable().getIntType()) ;

    Instruction ifAnotherElem =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, elem, oldElem) ,  tom.engine.adt.tominstruction.types.instruction.AssignArray.make(mult, counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetElement.make(intArrayName, intType, mult, counter) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make(counter) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(oldElem,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(elem) ,  tom.engine.adt.tominstruction.types.instruction.AssignArray.make(mult, counter,  tom.engine.adt.tomexpression.types.expression.Integer.make(1) ) ) ) ) 

;

    Instruction ifEndList =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, subject) ) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, subject, oldElem) ,  tom.engine.adt.tominstruction.types.instruction.AssignArray.make(mult, counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetElement.make(intArrayName, intType, mult, counter) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make(counter) ,  tom.engine.adt.tominstruction.types.instruction.AssignArray.make(mult, counter,  tom.engine.adt.tomexpression.types.expression.Integer.make(1) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 



;

    Instruction whileBlock =  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.LetRef.make(elem,  tom.engine.adt.tomexpression.types.expression.GetHead.make(opName, opType, subject) , ifAnotherElem) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(subject,  tom.engine.adt.tomexpression.types.expression.GetTail.make(opName, subject) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(ifEndList, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 



; 
    Expression notEmptySubj =  tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, subject,  tom.engine.adt.code.types.bqterm.BuildEmptyList.make(opName) ) ) ;
    Instruction whileLoop =  tom.engine.adt.tominstruction.types.instruction.WhileDo.make( tom.engine.adt.tomexpression.types.expression.And.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, subject) , notEmptySubj) , whileBlock) ;
    

    
    Instruction functionBody =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(length,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make(ConstraintGenerator.computeLengthFuncName+"_"+opNameString) , intType,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(subject, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(mult,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildEmptyArray.make(intArrayName, length) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(oldElem,  tom.engine.adt.tomexpression.types.expression.Bottom.make(opType) ,  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(ifList, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.LetRef.make(counter,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) , whileLoop) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(mult) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) ) ) 







;

    return  tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(ConstraintGenerator.multiplicityFuncName+"_"+opNameString) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(subject, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , intArrayType,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , functionBody) 
;
  }

  
  private Declaration getPILforComputeLength(String opNameString, TomType opType) {    
    
    BQTerm subject =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("subject") , opType) ;    
    BQTerm old =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("old") , opType) ;       
    BQTerm counter =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("counter") , getSymbolTable().getIntType()) ;
    BQTerm elem =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("elem") , opType) ;    
    
    Instruction isNewElem =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, elem, old) ) ,  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.LetRef.make(counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make(counter) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(old,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(elem) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 
;    

    TomName opName =  tom.engine.adt.tomname.types.tomname.Name.make(opNameString) ;
    
    Instruction isEndList =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, subject) ) ,  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, subject, old) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make(counter) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 
;

    Instruction whileBlock =  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.LetRef.make(elem,  tom.engine.adt.tomexpression.types.expression.GetHead.make(opName, opType, subject) , isNewElem) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(subject,  tom.engine.adt.tomexpression.types.expression.GetTail.make(opName, subject) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(isEndList, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 





; 
    Expression notEmptySubj =  tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, subject,  tom.engine.adt.code.types.bqterm.BuildEmptyList.make(opName) ) ) ;
    Instruction whileLoop =  tom.engine.adt.tominstruction.types.instruction.WhileDo.make( tom.engine.adt.tomexpression.types.expression.And.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, subject) , notEmptySubj) , whileBlock) ;
    

    
    Instruction isConsOpName =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, subject) ) ,  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Integer.make(1) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;

    Instruction functionBody =  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(isConsOpName, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.LetRef.make(old,  tom.engine.adt.tomexpression.types.expression.Bottom.make(opType) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(counter,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) ,  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(whileLoop, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(counter) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 




;

    return  tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(ConstraintGenerator.computeLengthFuncName+"_"+opNameString) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(subject, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , getSymbolTable().getIntType(),  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , functionBody) 
;
  }  

  
  private Declaration getPILforGetTermForMultiplicity(String opNameString, TomType opType) {

    TomType intArrayType = getSymbolTable().getIntArrayType();
    TomType boolType = getSymbolTable().getBooleanType();
    TomType intType = getSymbolTable().getIntType();

    
    BQTerm tempSol =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("tempSol") , intArrayType) ;      
    BQTerm subject =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("subject") , opType) ;
    BQTerm elem =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("elem") , opType) ;

    BQTerm elemCounter =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("elemCounter") , intType) ;

    
    TomName opName =  tom.engine.adt.tomname.types.tomname.Name.make(opNameString) ;
    Instruction isConsOpName =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, subject) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(elem,  tom.engine.adt.tomexpression.types.expression.GetHead.make(opName, opType, subject) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(subject,  tom.engine.adt.tomexpression.types.expression.GetTail.make(opName, subject) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(elem,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(subject) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(subject,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildEmptyList.make(opName) ) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) 






;
    BQTerm tempSolIndex =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("tempSolIndex") , intType) ;
    BQTerm old =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("old") , opType) ;
    Instruction isNewElem =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType, elem, old) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(tempSolIndex,  tom.engine.adt.tomexpression.types.expression.AddOne.make(tempSolIndex) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(old,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(elem) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(elemCounter,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 




;  
    
    BQTerm tempSolVal =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("tempSolVal") , intType) ;
    BQTerm alpha =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("alpha") , intArrayType) ; 
    BQTerm isComplement =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("isComplement") , boolType) ;
    TomName intArrayName =  tom.engine.adt.tomname.types.tomname.Name.make(getSymbolTable().getIntArrayOp()) ;
    Instruction ifIsComplement =  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(isComplement) ,  tom.engine.adt.tominstruction.types.instruction.Assign.make(tempSolVal,  tom.engine.adt.tomexpression.types.expression.Substract.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetElement.make(intArrayName, intType, alpha, tempSolIndex) ) , tempSolVal) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 



;

    
    Expression ifCond =  tom.engine.adt.tomexpression.types.expression.And.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(intType, tempSolVal,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Integer.make(0) ) ) ) ,  tom.engine.adt.tomexpression.types.expression.LessThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(elemCounter) ,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(tempSolVal) ) ) 
;
    BQTerm result =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("result") , opType) ;
    Instruction ifTakeElem =  tom.engine.adt.tominstruction.types.instruction.If.make(ifCond,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(result,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildAppendList.make(opName, result, elem) ) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(elemCounter,  tom.engine.adt.tomexpression.types.expression.AddOne.make(elemCounter) ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) 



;
    
    Instruction tempSolValBlock =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(tempSolVal,  tom.engine.adt.tomexpression.types.expression.GetElement.make(intArrayName, intType, tempSol, tempSolIndex) ,  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(ifIsComplement, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(ifTakeElem, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) 

;
    
    Expression notEmptySubj =  tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(opType,  tom.engine.adt.code.types.bqterm.BuildEmptyList.make(opName) , subject) ) ;
    Instruction whileBlock =  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(isConsOpName, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(isNewElem, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tempSolValBlock, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) 
;                  
    Instruction whileLoop =  tom.engine.adt.tominstruction.types.instruction.WhileDo.make(notEmptySubj, whileBlock) ;

    Instruction functionBody =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(result,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildEmptyList.make(opName) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(old,  tom.engine.adt.tomexpression.types.expression.Bottom.make(opType) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(elem,  tom.engine.adt.tomexpression.types.expression.Bottom.make(opType) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(elemCounter,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(tempSolIndex,  tom.engine.adt.tomexpression.types.expression.Integer.make(-1) ,  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(whileLoop, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Return.make(result) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ) ) ) ) 




; 

    return  tom.engine.adt.tomdeclaration.types.declaration.MethodDef.make( tom.engine.adt.tomname.types.tomname.Name.make(ConstraintGenerator.getTermForMultiplicityFuncName+"_"+opNameString) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tempSol, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(alpha, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(subject, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(isComplement, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ) , opType,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , functionBody) 
;
  } 
}
