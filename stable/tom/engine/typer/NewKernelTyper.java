

























package tom.engine.typer;



import java.util.*;
import java.util.logging.Logger;



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



import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;



import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;



import tom.library.sl.*;



public class NewKernelTyper {
     private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) ;}private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) ;}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make( tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) , null ) ) ) ) );}   private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_append_list_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList l1,  tom.engine.adt.typeconstraints.types.TypeConstraintList  l2) {     if( l1.isEmptyconcTypeConstraint() ) {       return l2;     } else if( l2.isEmptyconcTypeConstraint() ) {       return l1;     } else if(  l1.getTailconcTypeConstraint() .isEmptyconcTypeConstraint() ) {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,l2) ;     } else {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,tom_append_list_concTypeConstraint( l1.getTailconcTypeConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_get_slice_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList  begin,  tom.engine.adt.typeconstraints.types.TypeConstraintList  end, tom.engine.adt.typeconstraints.types.TypeConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeConstraint()  ||  (end== tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( begin.getHeadconcTypeConstraint() ,( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_get_slice_concTypeConstraint( begin.getTailconcTypeConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {     if( l1.isEmptyconcTypeOption() ) {       return l2;     } else if( l2.isEmptyconcTypeOption() ) {       return l1;     } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {     if( l1.isEmptyconcCode() ) {       return l2;     } else if( l2.isEmptyconcCode() ) {       return l1;     } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;     } else {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }   


  private static Logger logger = Logger.getLogger("tom.engine.typer.NewKernelTyper");

  




  
  
  private TomList varPatternList;
  
  private BQTermList varList;

  
  private TomTypeList inputTVarList;
  
  private TypeConstraintList equationConstraints;
  
  private TypeConstraintList subtypeConstraints;
  
  private Collection<TypeConstraint> constraintBag;

  
  private Substitution substitutions;
  
  private HashMap<String,TomTypeList> dependencies = new HashMap<String,TomTypeList>();

  private SymbolTable symbolTable;

  private String currentInputFileName;

  protected void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  protected void setCurrentInputFileName(String currentInputFileName) {
    this.currentInputFileName = currentInputFileName;
  }

  protected String getCurrentInputFileName() {
    return currentInputFileName;
  }
  protected TomType getCodomain(TomSymbol tSymbol) {
    return TomBase.getSymbolCodomain(tSymbol);
  }

  protected TomSymbol getSymbolFromTerm(TomTerm tTerm) {
    return TomBase.getSymbolFromTerm(tTerm, symbolTable);
  }

  protected TomSymbol getSymbolFromTerm(BQTerm bqTerm) {
    return TomBase.getSymbolFromTerm(bqTerm,symbolTable);
  }

  protected TomSymbol getSymbolFromName(String tName) {
    return TomBase.getSymbolFromName(tName, symbolTable);
  }


  


  


  
  protected Info getInfoFromTomTerm(TomTerm tTerm) {
    { /* unamed block */{ /* unamed block */if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getInfoFromTomTerm( (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getTomTerm() ); }}}{ /* unamed block */if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch480_10= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch480_9= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch480_8= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch480_5= null ; tom.engine.adt.tomname.types.TomName  tomMatch480_6= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch480_10= true ;tomMatch480_8=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch480_5= tomMatch480_8.getOptions() ;tomMatch480_6= tomMatch480_8.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch480_10= true ;tomMatch480_9=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch480_5= tomMatch480_9.getOptions() ;tomMatch480_6= tomMatch480_9.getAstName() ;}}}if (tomMatch480_10) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch480_6, tomMatch480_5) ; 
      }}}{ /* unamed block */if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch480_13= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch480_13) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch480_13) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch480_13.isEmptyconcTomName() )) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch480_13.getHeadconcTomName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ) ; 
      }}}}}}
 
    throw new TomRuntimeException("getInfoFromTomTerm: should not be here: " + tTerm);
    
  }

  
  protected Info getInfoFromBQTerm(BQTerm bqTerm) {
    { /* unamed block */{ /* unamed block */if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch481_7= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch481_1= null ; tom.engine.adt.tomname.types.TomName  tomMatch481_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_5= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_6= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch481_7= true ;tomMatch481_4=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_1= tomMatch481_4.getOptions() ;tomMatch481_2= tomMatch481_4.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch481_7= true ;tomMatch481_5=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_1= tomMatch481_5.getOptions() ;tomMatch481_2= tomMatch481_5.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{ /* unamed block */tomMatch481_7= true ;tomMatch481_6=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_1= tomMatch481_6.getOptions() ;tomMatch481_2= tomMatch481_6.getAstName() ;}}}}if (tomMatch481_7) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch481_2, tomMatch481_1) ; 
      }}}{ /* unamed block */if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch481_end_12=(( tom.engine.adt.code.types.BQTerm )bqTerm);do {{ /* unamed block */if (!( tomMatch481_end_12.isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tomMatch481_16= tomMatch481_end_12.getHeadComposite() ;if ( ((( tom.engine.adt.code.types.CompositeMember )tomMatch481_16) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {


        return getInfoFromBQTerm( tomMatch481_16.getterm() );
      }}if ( tomMatch481_end_12.isEmptyComposite() ) {tomMatch481_end_12=(( tom.engine.adt.code.types.BQTerm )bqTerm);} else {tomMatch481_end_12= tomMatch481_end_12.getTailComposite() ;}}} while(!( (tomMatch481_end_12==(( tom.engine.adt.code.types.BQTerm )bqTerm)) ));}}}{ /* unamed block */if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch481_29= false ; tom.engine.adt.code.types.BQTerm  tomMatch481_25= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_23= null ; tom.engine.adt.tomname.types.TomName  tomMatch481_19= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_22= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_27= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_21= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_24= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_26= null ; tom.engine.adt.code.types.BQTerm  tomMatch481_28= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_21=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_21.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_22=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_22.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_23=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_23.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_24=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_24.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_25=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_25.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_26=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_26.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_27=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_27.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {{ /* unamed block */tomMatch481_29= true ;tomMatch481_28=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch481_19= tomMatch481_28.getAstName() ;}}}}}}}}}if (tomMatch481_29) {


        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch481_19,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ; 
      }}}}
 
    throw new TomRuntimeException("getInfoFromTomBQTerm: should not be here: " + bqTerm);
    
  }

  
  private int limTVarSymbolTable;
  protected void setLimTVarSymbolTable(int freshTVarSymbolTable) {
    limTVarSymbolTable = freshTVarSymbolTable;
    globalTypeVarCounter = freshTVarSymbolTable; 
  }

  
  private int globalEqConstraintCounter = 0;
  private int globalSubConstraintCounter = 0;
  private int globalTypeVarCounter = 0;
  protected int getEqCounter() { return globalEqConstraintCounter; } 
  protected int getSubCounter() { return globalSubConstraintCounter; } 
  protected int getTVarCounter() { return globalTypeVarCounter; }

  
  private int freshTypeVarCounter;
  private int getFreshTlTIndex() {
    globalTypeVarCounter++;
    return freshTypeVarCounter++;
  }

  
  protected TomType getUnknownFreshTypeVar() {
    TomType tType = symbolTable.TYPE_UNKNOWN;
    { /* unamed block */{ /* unamed block */if ( (tType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 return  tom.engine.adt.tomtype.types.tomtype.TypeVar.make( (( tom.engine.adt.tomtype.types.TomType )tType).getTomType() , getFreshTlTIndex()) ; }}}}

    throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
  }


  protected TypeConstraintList addConstraintSlow(TypeConstraint tConstraint, TypeConstraintList tCList) {
    if(!containsConstraint(tConstraint,tCList)) {
      { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch483_14= false ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch483_4= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch483_5= null ; tom.engine.adt.tomtype.types.TomType  tomMatch483_1= null ; tom.engine.adt.tomtype.types.TomType  tomMatch483_2= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{ /* unamed block */tomMatch483_14= true ;tomMatch483_4=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch483_1= tomMatch483_4.getType1() ;tomMatch483_2= tomMatch483_4.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{ /* unamed block */tomMatch483_14= true ;tomMatch483_5=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch483_1= tomMatch483_5.getType1() ;tomMatch483_2= tomMatch483_5.getType2() ;}}}if (tomMatch483_14) { tom.engine.adt.tomtype.types.TomType  tom___t1=tomMatch483_1; tom.engine.adt.tomtype.types.TomType  tom___t2=tomMatch483_2;boolean tomMatch483_13= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch483_2) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom___t2==tomMatch483_2) ) {tomMatch483_13= true ;}}if (!(tomMatch483_13)) {boolean tomMatch483_12= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch483_1) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom___t1==tomMatch483_1) ) {tomMatch483_12= true ;}}if (!(tomMatch483_12)) {if (!( (tom___t2==tom___t1) )) {
 
          tCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
        }}}}}}}

    }
    return tCList;
  }

  
  protected boolean containsConstraint(TypeConstraint tConstraint, TypeConstraintList tCList) {
    { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch484_end_9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch484_end_9.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch484_14= tomMatch484_end_9.getHeadconcTypeConstraint() ;boolean tomMatch484_19= false ; tom.engine.adt.tomtype.types.TomType  tomMatch484_13= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch484_16= null ; tom.engine.adt.tomtype.types.TomType  tomMatch484_12= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch484_15= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch484_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{ /* unamed block */tomMatch484_19= true ;tomMatch484_15=tomMatch484_14;tomMatch484_12= tomMatch484_15.getType1() ;tomMatch484_13= tomMatch484_15.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch484_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{ /* unamed block */tomMatch484_19= true ;tomMatch484_16=tomMatch484_14;tomMatch484_12= tomMatch484_16.getType1() ;tomMatch484_13= tomMatch484_16.getType2() ;}}}if (tomMatch484_19) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ==tomMatch484_12) ) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ==tomMatch484_13) ) {
 return true; }}}}if ( tomMatch484_end_9.isEmptyconcTypeConstraint() ) {tomMatch484_end_9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch484_end_9= tomMatch484_end_9.getTailconcTypeConstraint() ;}}} while(!( (tomMatch484_end_9==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tom___t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tom___t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch484_end_29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch484_end_29.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch484_34= tomMatch484_end_29.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch484_34) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tom___t3= tomMatch484_34.getType1() ; tom.engine.adt.tomtype.types.TomType  tom___t4= tomMatch484_34.getType2() ;


        if((tom___t1==tom___t3&&tom___t2==tom___t4) || (tom___t1==tom___t4&&tom___t2==tom___t3)) { /* unamed block */
          return true; 
        }}}if ( tomMatch484_end_29.isEmptyconcTypeConstraint() ) {tomMatch484_end_29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch484_end_29= tomMatch484_end_29.getTailconcTypeConstraint() ;}}} while(!( (tomMatch484_end_29==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}


    return containsConstraintModuloEqDecoratedSort(tConstraint,tCList);
  } 
  
  
  protected boolean containsConstraintModuloEqDecoratedSort(TypeConstraint tConstraint, TypeConstraintList tCList) {
    { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_4= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch485_5= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_4) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_5) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions= tomMatch485_5.getTypeOptions() ;if ( (tom___tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch485_end_17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch485_end_17.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch485_28= tomMatch485_end_17.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch485_28) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_27= tomMatch485_28.getType2() ;if ( (tomMatch485_4== tomMatch485_28.getType1() ) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_27) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___decoratedtOptions= tomMatch485_27.getTypeOptions() ;if (  tomMatch485_5.getTomType() .equals( tomMatch485_27.getTomType() ) ) {if ( (tom___decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_23=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);do {{ /* unamed block */if (!( tomMatch485_end_23.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_23.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch485_46= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_39=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);do {{ /* unamed block */if (!( tomMatch485_end_39.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_39.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch485_46= true ;}}if ( tomMatch485_end_39.isEmptyconcTypeOption() ) {tomMatch485_end_39=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);} else {tomMatch485_end_39= tomMatch485_end_39.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_39==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions)) ));}if (!(tomMatch485_46)) {



 return true; }}}if ( tomMatch485_end_23.isEmptyconcTypeOption() ) {tomMatch485_end_23=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);} else {tomMatch485_end_23= tomMatch485_end_23.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_23==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions)) ));}}}}}}}if ( tomMatch485_end_17.isEmptyconcTypeConstraint() ) {tomMatch485_end_17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch485_end_17= tomMatch485_end_17.getTailconcTypeConstraint() ;}}} while(!( (tomMatch485_end_17==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_51= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch485_52= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_51) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_52) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions= tomMatch485_52.getTypeOptions() ;if ( (tom___tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch485_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch485_end_64.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch485_75= tomMatch485_end_64.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch485_75) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_73= tomMatch485_75.getType1() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_73) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___decoratedtOptions= tomMatch485_73.getTypeOptions() ;if (  tomMatch485_52.getTomType() .equals( tomMatch485_73.getTomType() ) ) {if ( (tomMatch485_51== tomMatch485_75.getType2() ) ) {if ( (tom___decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_70=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);do {{ /* unamed block */if (!( tomMatch485_end_70.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_70.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch485_93= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_86=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);do {{ /* unamed block */if (!( tomMatch485_end_86.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_86.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch485_93= true ;}}if ( tomMatch485_end_86.isEmptyconcTypeOption() ) {tomMatch485_end_86=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);} else {tomMatch485_end_86= tomMatch485_end_86.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_86==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions)) ));}if (!(tomMatch485_93)) {




 return true; }}}if ( tomMatch485_end_70.isEmptyconcTypeOption() ) {tomMatch485_end_70=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);} else {tomMatch485_end_70= tomMatch485_end_70.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_70==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions)) ));}}}}}}}if ( tomMatch485_end_64.isEmptyconcTypeConstraint() ) {tomMatch485_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch485_end_64= tomMatch485_end_64.getTailconcTypeConstraint() ;}}} while(!( (tomMatch485_end_64==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_98= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch485_99= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_98) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions= tomMatch485_98.getTypeOptions() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_99) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch485_end_111=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch485_end_111.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch485_122= tomMatch485_end_111.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch485_122) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_121= tomMatch485_122.getType2() ;if ( (tomMatch485_99== tomMatch485_122.getType1() ) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_121) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___decoratedtOptions= tomMatch485_121.getTypeOptions() ;if (  tomMatch485_98.getTomType() .equals( tomMatch485_121.getTomType() ) ) {if ( (tom___decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_117=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);do {{ /* unamed block */if (!( tomMatch485_end_117.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_117.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch485_140= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_133=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);do {{ /* unamed block */if (!( tomMatch485_end_133.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_133.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch485_140= true ;}}if ( tomMatch485_end_133.isEmptyconcTypeOption() ) {tomMatch485_end_133=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);} else {tomMatch485_end_133= tomMatch485_end_133.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_133==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions)) ));}if (!(tomMatch485_140)) {




 return true; }}}if ( tomMatch485_end_117.isEmptyconcTypeOption() ) {tomMatch485_end_117=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);} else {tomMatch485_end_117= tomMatch485_end_117.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_117==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions)) ));}}}}}}}if ( tomMatch485_end_111.isEmptyconcTypeConstraint() ) {tomMatch485_end_111=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch485_end_111= tomMatch485_end_111.getTailconcTypeConstraint() ;}}} while(!( (tomMatch485_end_111==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_145= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch485_146= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_145) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions= tomMatch485_145.getTypeOptions() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_146) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch485_end_158=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch485_end_158.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch485_169= tomMatch485_end_158.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch485_169) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch485_167= tomMatch485_169.getType1() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch485_167) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___decoratedtOptions= tomMatch485_167.getTypeOptions() ;if (  tomMatch485_145.getTomType() .equals( tomMatch485_167.getTomType() ) ) {if ( (tomMatch485_146== tomMatch485_169.getType2() ) ) {if ( (tom___decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_164=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);do {{ /* unamed block */if (!( tomMatch485_end_164.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_164.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch485_187= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch485_end_180=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);do {{ /* unamed block */if (!( tomMatch485_end_180.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch485_end_180.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch485_187= true ;}}if ( tomMatch485_end_180.isEmptyconcTypeOption() ) {tomMatch485_end_180=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions);} else {tomMatch485_end_180= tomMatch485_end_180.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_180==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions)) ));}if (!(tomMatch485_187)) {





 return true; }}}if ( tomMatch485_end_164.isEmptyconcTypeOption() ) {tomMatch485_end_164=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions);} else {tomMatch485_end_164= tomMatch485_end_164.getTailconcTypeOption() ;}}} while(!( (tomMatch485_end_164==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___decoratedtOptions)) ));}}}}}}}if ( tomMatch485_end_158.isEmptyconcTypeConstraint() ) {tomMatch485_end_158=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch485_end_158= tomMatch485_end_158.getTailconcTypeConstraint() ;}}} while(!( (tomMatch485_end_158==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}}

    return false;
  }


  
  
  protected TypeConstraintList addConstraint(TypeConstraint tConstraint, TypeConstraintList tCList) {
    TypeConstraint constraint = null;
    TypeOptionList emptyOptionList =  tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ;
    TargetLanguageType emptyTargetLanguageType =  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ;
    Info emptyInfo =  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.EmptyName.make() ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ;

    { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() , emptyInfo) ;
      }}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch486_6= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch486_7= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_6) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_7) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tomMatch486_6,  tom.engine.adt.tomtype.types.tomtype.Type.make(emptyOptionList,  tomMatch486_7.getTomType() , emptyTargetLanguageType) , emptyInfo) ;
      }}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch486_16= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch486_17= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_16) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_17) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( tom.engine.adt.tomtype.types.tomtype.Type.make(emptyOptionList,  tomMatch486_16.getTomType() , emptyTargetLanguageType) , tomMatch486_17, emptyInfo) ;
      }}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch486_26= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch486_27= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_26) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_27) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {


        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch486_26.getTypeOptions() ,  tomMatch486_26.getTomType() , emptyTargetLanguageType) ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch486_27.getTypeOptions() ,  tomMatch486_27.getTomType() , emptyTargetLanguageType) , emptyInfo) 
;
      }}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {


        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() , emptyInfo) ;
      }}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch486_45= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_45) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch486_45.getTypeOptions() ,  tomMatch486_45.getTomType() , emptyTargetLanguageType) , emptyInfo) ;
      }}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch486_53= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_53) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch486_53.getTypeOptions() ,  tomMatch486_53.getTomType() , emptyTargetLanguageType) ,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() , emptyInfo) ;
      }}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch486_62= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch486_63= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_62) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_63) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch486_62.getTypeOptions() ,  tomMatch486_62.getTomType() , emptyTargetLanguageType) ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch486_63.getTypeOptions() ,  tomMatch486_63.getTomType() , emptyTargetLanguageType) , emptyInfo) 
;
      }}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch486_81= false ; tom.engine.adt.tomtype.types.TomType  tomMatch486_76= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch486_79= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch486_78= null ; tom.engine.adt.tomtype.types.TomType  tomMatch486_75= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{ /* unamed block */tomMatch486_81= true ;tomMatch486_78=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch486_75= tomMatch486_78.getType1() ;tomMatch486_76= tomMatch486_78.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{ /* unamed block */tomMatch486_81= true ;tomMatch486_79=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch486_75= tomMatch486_79.getType1() ;tomMatch486_76= tomMatch486_79.getType2() ;}}}if (tomMatch486_81) {if ( (tomMatch486_75==tomMatch486_76) ) {


        
        constraint         = null;
      }}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch486_90= false ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch486_87= null ; tom.engine.adt.tomtype.types.TomType  tomMatch486_83= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch486_86= null ; tom.engine.adt.tomtype.types.TomType  tomMatch486_84= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{ /* unamed block */tomMatch486_90= true ;tomMatch486_86=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch486_83= tomMatch486_86.getType1() ;tomMatch486_84= tomMatch486_86.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{ /* unamed block */tomMatch486_90= true ;tomMatch486_87=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch486_83= tomMatch486_87.getType1() ;tomMatch486_84= tomMatch486_87.getType2() ;}}}if (tomMatch486_90) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_83) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {

        
        constraint         = null;
      }}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch486_99= false ; tom.engine.adt.tomtype.types.TomType  tomMatch486_93= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch486_95= null ; tom.engine.adt.tomtype.types.TomType  tomMatch486_92= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch486_96= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{ /* unamed block */tomMatch486_99= true ;tomMatch486_95=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch486_92= tomMatch486_95.getType1() ;tomMatch486_93= tomMatch486_95.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{ /* unamed block */tomMatch486_99= true ;tomMatch486_96=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch486_92= tomMatch486_96.getType1() ;tomMatch486_93= tomMatch486_96.getType2() ;}}}if (tomMatch486_99) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch486_93) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {

        
        constraint         = null;
      }}}}}



    if(constraint == null || constraintBag.contains(constraint)) {
      return tCList;
    } else {
      constraintBag.add(constraint);
      { /* unamed block */{ /* unamed block */if ( (constraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )constraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {

          constraintBag.add( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( (( tom.engine.adt.typeconstraints.types.TypeConstraint )constraint).getType2() ,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )constraint).getType1() ,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )constraint).getInfo() ) );
        }}}}


      return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
    }
   
  }


  
  
  
  
  
  protected void generateDependencies() {
    for(TomType currentType:symbolTable.getUsedTypes()) {
      String currentTypeName = currentType.getTomType();
      TomTypeList superTypes =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
      { /* unamed block */{ /* unamed block */if ( (currentType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )currentType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch488_1= (( tom.engine.adt.tomtype.types.TomType )currentType).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch488_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch488_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch488_end_8=tomMatch488_1;do {{ /* unamed block */if (!( tomMatch488_end_8.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch488_12= tomMatch488_end_8.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch488_12) instanceof tom.engine.adt.tomtype.types.typeoption.SubtypeDecl) ) { String  tom___supTypeName= tomMatch488_12.getTomType() ;


          
          
          if(dependencies.containsKey(tom___supTypeName)) { /* unamed block */
            superTypes = dependencies.get(tom___supTypeName); 
          }
          TomType supType = symbolTable.getType(tom___supTypeName);
          if(supType != null) { /* unamed block */
            superTypes =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(supType,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;  

            
            for(String subType:dependencies.keySet()) { /* unamed block */
              TomTypeList supOfSubTypes = dependencies.get(subType);
              
              
              { /* unamed block */{ /* unamed block */if ( (supOfSubTypes instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch489_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes);do {{ /* unamed block */if (!( tomMatch489_end_4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch489_8= tomMatch489_end_4.getHeadconcTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch489_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  (( tom.engine.adt.tomtype.types.TomType )currentType).getTomType() .equals( tomMatch489_8.getTomType() ) ) {


                    
                    dependencies.put(subType,tom_append_list_concTomType(supOfSubTypes,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )));
                  }}}if ( tomMatch489_end_4.isEmptyconcTomType() ) {tomMatch489_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes);} else {tomMatch489_end_4= tomMatch489_end_4.getTailconcTomType() ;}}} while(!( (tomMatch489_end_4==(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes)) ));}}}}}}


 else { /* unamed block */
            TomMessage.error(logger,getCurrentInputFileName(),0,
                TomMessage.typetermNotDefined,tom___supTypeName);
          }}}if ( tomMatch488_end_8.isEmptyconcTypeOption() ) {tomMatch488_end_8=tomMatch488_1;} else {tomMatch488_end_8= tomMatch488_end_8.getTailconcTypeOption() ;}}} while(!( (tomMatch488_end_8==tomMatch488_1) ));}}}}}


      
      
      dependencies.put(currentTypeName,superTypes);
    }
  }


  protected void addPositiveTVar(TomType tType) {
    inputTVarList =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(tType,tom_append_list_concTomType(inputTVarList, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;
  }

  
  protected void addTomTerm(TomTerm tTerm) {
    varPatternList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tTerm,tom_append_list_concTomTerm(varPatternList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
  }

  
  protected void addBQTerm(BQTerm bqTerm) {
    varList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(bqTerm,tom_append_list_concBQTerm(varList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
  }

  
  protected void resetVarList(TomList globalVarPatternList) {
    for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {
      { /* unamed block */{ /* unamed block */if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch490_27= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch490_5= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch490_6= null ; tom.engine.adt.tomname.types.TomName  tomMatch490_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch490_27= true ;tomMatch490_5=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch490_3= tomMatch490_5.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch490_27= true ;tomMatch490_6=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch490_3= tomMatch490_6.getAstName() ;}}}if (tomMatch490_27) {if ( (globalVarPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch490_end_10=(( tom.engine.adt.code.types.BQTermList )varList);do {{ /* unamed block */if (!( tomMatch490_end_10.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch490_14= tomMatch490_end_10.getHeadconcBQTerm() ;boolean tomMatch490_26= false ; tom.engine.adt.tomname.types.TomName  tomMatch490_13= null ; tom.engine.adt.code.types.BQTerm  tomMatch490_15= null ; tom.engine.adt.code.types.BQTerm  tomMatch490_16= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch490_14) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch490_26= true ;tomMatch490_15=tomMatch490_14;tomMatch490_13= tomMatch490_15.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch490_14) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch490_26= true ;tomMatch490_16=tomMatch490_14;tomMatch490_13= tomMatch490_16.getAstName() ;}}}if (tomMatch490_26) {if ( (tomMatch490_3==tomMatch490_13) ) {boolean tomMatch490_25= false ;if ( (((( tom.engine.adt.tomterm.types.TomList )globalVarPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )globalVarPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch490_end_20=(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList);do {{ /* unamed block */if (!( tomMatch490_end_20.isEmptyconcTomTerm() )) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm)== tomMatch490_end_20.getHeadconcTomTerm() ) ) {tomMatch490_25= true ;}}if ( tomMatch490_end_20.isEmptyconcTomTerm() ) {tomMatch490_end_20=(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList);} else {tomMatch490_end_20= tomMatch490_end_20.getTailconcTomTerm() ;}}} while(!( (tomMatch490_end_20==(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList)) ));}if (!(tomMatch490_25)) {




            
            varList = tom_append_list_concBQTerm(tom_get_slice_concBQTerm((( tom.engine.adt.code.types.BQTermList )varList),tomMatch490_end_10, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ),tom_append_list_concBQTerm( tomMatch490_end_10.getTailconcBQTerm() , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));
          }}}}if ( tomMatch490_end_10.isEmptyconcBQTerm() ) {tomMatch490_end_10=(( tom.engine.adt.code.types.BQTermList )varList);} else {tomMatch490_end_10= tomMatch490_end_10.getTailconcBQTerm() ;}}} while(!( (tomMatch490_end_10==(( tom.engine.adt.code.types.BQTermList )varList)) ));}}}}}}}

    }
  }

  
  public void init() {
    freshTypeVarCounter = limTVarSymbolTable;
    varPatternList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
    varList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    inputTVarList =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
    equationConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    subtypeConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    constraintBag = new HashSet<TypeConstraint>();
    substitutions = new Substitution();
  }

  
  private Code collectKnownTypesFromCode(Code subject) {
    try {
      return tom_make_TopDownIdStopOnSuccess( new CollectKnownTypes(this) ).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  
  public static class CollectKnownTypes extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public CollectKnownTypes( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___tomType= (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {


        TomType newType = nkt.symbolTable.getType(tom___tomType);
        if(newType == null) { /* unamed block */
          
          newType =  tom.engine.adt.tomtype.types.tomtype.TypeVar.make(tom___tomType, nkt.getFreshTlTIndex()) ;
        }
        return newType;
      }}}}}return _visit_TomType(tom__arg,introspector);}}



  
  public <T extends tom.library.sl.Visitable> T inferAllTypes(T term, TomType contextType) {
    try {
      
      return tom_make_TopDownStopOnSuccess( new inferTypes(contextType,this) ).visitLight(term); 
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("inferAllTypes: failure on " + term);
    }
  }

  
  public static class inferTypes extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  contextType;private  NewKernelTyper  nkt;public inferTypes( tom.engine.adt.tomtype.types.TomType  contextType,  NewKernelTyper  nkt) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.nkt=nkt;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.Code) ) {return ((T)visit_Code((( tom.engine.adt.code.types.Code )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.Code  _visit_Code( tom.engine.adt.code.types.Code  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.Code )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.code.types.BQTerm  tom___bqVar=(( tom.engine.adt.code.types.BQTerm )tom__arg);
















































































































        nkt.checkNonLinearityOfBQVariables(tom___bqVar);
        nkt.subtypeConstraints = nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ) ) ,nkt.subtypeConstraints);  
        return tom___bqVar;
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.code.types.BQTerm  tom___bqVarStar=(( tom.engine.adt.code.types.BQTerm )tom__arg);


        nkt.checkNonLinearityOfBQVariables(tom___bqVarStar);
        nkt.equationConstraints =
          nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ) ) ,nkt.equationConstraints);
        return tom___bqVarStar;
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch492_14= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom___optionList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch492_14) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom___aName=tomMatch492_14; tom.engine.adt.code.types.BQTermList  tom___bqTList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getArgs() ;


        TomSymbol tSymbol = nkt.getSymbolFromName( tomMatch492_14.getString() );
        TomType codomain = contextType;
        if (tSymbol == null) { /* unamed block */
          tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
          
          
          BQTermList newBQTList = nkt.inferBQTermList(tom___bqTList, tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ,contextType);
          
          return  tom.engine.adt.code.types.bqterm.FunctionCall.make(tom___aName, contextType, newBQTList) ; 
        } else { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch493_2= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom___symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch493_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch493_8= tomMatch493_2.getCodomain() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch493_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch493_11= tomMatch493_8.getTypeOptions() ;if ( (tomMatch493_11 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {

 
              codomain = tomMatch493_8;
              if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) { /* unamed block */
                
                TypeOptionList newTOptions =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom___symName) ,tom_append_list_concTypeOption(tomMatch493_11, tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;
                codomain =  tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch493_8.getTomType() ,  tomMatch493_8.getTlType() ) ;
                tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom___symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch493_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getOptions() ) ; 
              }}}}}}}}


          nkt.subtypeConstraints = nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,nkt.subtypeConstraints);

          BQTermList newBQTList = nkt.inferBQTermList(tom___bqTList,tSymbol,contextType);
          return  tom.engine.adt.code.types.bqterm.BQAppl.make(tom___optionList, tom___aName, newBQTList) ;
        }}}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomtype.types.TomType  tom___aType= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom___cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ; tom.engine.adt.tomterm.types.TomTerm  tom___var=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);         nkt.checkNonLinearityOfVariables(tom___var);         nkt.subtypeConstraints =           nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ) ) ,nkt.subtypeConstraints);           { /* unamed block */{ /* unamed block */if ( (tom___cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch495_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).getHeadconcConstraint() ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch495_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom___boundTerm= tomMatch495_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(TomBase.getTermType(tom___boundTerm,nkt.symbolTable), tom___aType, nkt.getInfoFromTomTerm(tom___boundTerm)) ,nkt.equationConstraints);            }}}}}}}         return tom___var.setConstraints(tom___cList);       }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomtype.types.TomType  tom___aType= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom___cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ; tom.engine.adt.tomterm.types.TomTerm  tom___varStar=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);         nkt.checkNonLinearityOfVariables(tom___varStar);         nkt.equationConstraints =           nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom___aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ) ) ,nkt.equationConstraints);           { /* unamed block */{ /* unamed block */if ( (tom___cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch496_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).getHeadconcConstraint() ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch496_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom___boundTerm= tomMatch496_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(TomBase.getTermType(tom___boundTerm,nkt.symbolTable), tom___aType, nkt.getInfoFromTomTerm(tom___boundTerm)) ,nkt.equationConstraints);            }}}}}}}         return tom___varStar.setConstraints(tom___cList);       }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch494_16= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom___optionList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch494_16) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch494_16) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch494_16.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch494_25= tomMatch494_16.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch494_25) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomslot.types.SlotList  tom___sList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlots() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom___cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;                  TomSymbol tSymbol = nkt.getSymbolFromName( tomMatch494_25.getString() );         TomType codomain = contextType;                   if(tSymbol == null) { /* unamed block */           tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;         } else { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch497_2= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom___symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch497_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch497_8= tomMatch497_2.getCodomain() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch497_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch497_11= tomMatch497_8.getTypeOptions() ;if ( (tomMatch497_11 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {                codomain = tomMatch497_8;               if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) { /* unamed block */                                  TypeOptionList newTOptions =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom___symName) ,tom_append_list_concTypeOption(tomMatch497_11, tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;                 codomain =  tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch497_8.getTomType() ,  tomMatch497_8.getTlType() ) ;                 tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom___symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch497_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getOptions() ) ;                }}}}}}}}           nkt.subtypeConstraints = nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch494_16.getHeadconcTomName() , tom___optionList) ) ,nkt.subtypeConstraints);         }{ /* unamed block */{ /* unamed block */if ( (tom___cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch498_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).getHeadconcConstraint() ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch498_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom___boundTerm= tomMatch498_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom___cList).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(TomBase.getTermType(tom___boundTerm,nkt.symbolTable), codomain, nkt.getInfoFromTomTerm(tom___boundTerm)) ,nkt.equationConstraints);            }}}}}}}          SlotList newSList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;         if(!tom___sList.isEmptyconcSlot()) { /* unamed block */newSList = nkt.inferSlotList(tom___sList,tSymbol,contextType);         }         return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom___optionList, tomMatch494_16, newSList, tom___cList) ;       }}}}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tom__arg) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) {                  ConstraintInstructionList newCIList = nkt.inferConstraintInstructionList( (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getAstConstraintInstructionList() );                  return  tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make( (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getVNode() , newCIList,  (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getOptions() ) ;       }}}}return _visit_TomVisit(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {                  ConstraintInstructionList newCIList = nkt.inferConstraintInstructionList( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() );                  return  tom.engine.adt.tominstruction.types.instruction.Match.make(newCIList,  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() ) ;       }}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.Code  visit_Code( tom.engine.adt.code.types.Code  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.Code) ) {boolean tomMatch501_5= false ; tom.engine.adt.code.types.CodeList  tomMatch501_1= null ; tom.engine.adt.code.types.Code  tomMatch501_3= null ; tom.engine.adt.code.types.Code  tomMatch501_4= null ;if ( ((( tom.engine.adt.code.types.Code )tom__arg) instanceof tom.engine.adt.code.types.code.Tom) ) {{ /* unamed block */tomMatch501_5= true ;tomMatch501_3=(( tom.engine.adt.code.types.Code )tom__arg);tomMatch501_1= tomMatch501_3.getCodeList() ;}} else {if ( ((( tom.engine.adt.code.types.Code )tom__arg) instanceof tom.engine.adt.code.types.code.TomInclude) ) {{ /* unamed block */tomMatch501_5= true ;tomMatch501_4=(( tom.engine.adt.code.types.Code )tom__arg);tomMatch501_1= tomMatch501_4.getCodeList() ;}}}if (tomMatch501_5) {         nkt.generateDependencies();         CodeList newCList = nkt.inferCodeList(tomMatch501_1);         return (( tom.engine.adt.code.types.Code )tom__arg).setCodeList(newCList);       }}}}return _visit_Code(tom__arg,introspector);}}




  
  private void checkNonLinearityOfVariables(TomTerm var) {
    { /* unamed block */{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch502_41= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_7= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch502_3= null ; tom.engine.adt.tomtype.types.TomType  tomMatch502_5= null ; tom.engine.adt.tomname.types.TomName  tomMatch502_4= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_8= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch502_41= true ;tomMatch502_7=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch502_3= tomMatch502_7.getOptions() ;tomMatch502_4= tomMatch502_7.getAstName() ;tomMatch502_5= tomMatch502_7.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch502_41= true ;tomMatch502_8=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch502_3= tomMatch502_8.getOptions() ;tomMatch502_4= tomMatch502_8.getAstName() ;tomMatch502_5= tomMatch502_8.getAstType() ;}}}if (tomMatch502_41) { tom.engine.adt.tomoption.types.OptionList  tom___optionList=tomMatch502_3; tom.engine.adt.tomname.types.TomName  tom___aName=tomMatch502_4; tom.engine.adt.tomtype.types.TomType  tom___aType1=tomMatch502_5;if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch502_end_12=(( tom.engine.adt.tomterm.types.TomList )varPatternList);do {{ /* unamed block */if (!( tomMatch502_end_12.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch502_23= tomMatch502_end_12.getHeadconcTomTerm() ;boolean tomMatch502_38= false ; tom.engine.adt.tomname.types.TomName  tomMatch502_21= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_25= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_24= null ; tom.engine.adt.tomtype.types.TomType  tomMatch502_22= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch502_23) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch502_38= true ;tomMatch502_24=tomMatch502_23;tomMatch502_21= tomMatch502_24.getAstName() ;tomMatch502_22= tomMatch502_24.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch502_23) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch502_38= true ;tomMatch502_25=tomMatch502_23;tomMatch502_21= tomMatch502_25.getAstName() ;tomMatch502_22= tomMatch502_25.getAstType() ;}}}if (tomMatch502_38) {if ( (tom___aName==tomMatch502_21) ) { tom.engine.adt.tomtype.types.TomType  tom___aType2=tomMatch502_22;boolean tomMatch502_37= false ;if ( (tom___aType1==tomMatch502_22) ) {if ( (tom___aType2==tomMatch502_22) ) {tomMatch502_37= true ;}}if (!(tomMatch502_37)) {






          equationConstraints = addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom___aType1, tom___aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,equationConstraints);
        }}}}if ( tomMatch502_end_12.isEmptyconcTomTerm() ) {tomMatch502_end_12=(( tom.engine.adt.tomterm.types.TomList )varPatternList);} else {tomMatch502_end_12= tomMatch502_end_12.getTailconcTomTerm() ;}}} while(!( (tomMatch502_end_12==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));}}if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch502_end_18=(( tom.engine.adt.code.types.BQTermList )varList);do {{ /* unamed block */if (!( tomMatch502_end_18.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch502_28= tomMatch502_end_18.getHeadconcBQTerm() ;boolean tomMatch502_40= false ; tom.engine.adt.tomname.types.TomName  tomMatch502_26= null ; tom.engine.adt.tomtype.types.TomType  tomMatch502_27= null ; tom.engine.adt.code.types.BQTerm  tomMatch502_29= null ; tom.engine.adt.code.types.BQTerm  tomMatch502_30= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch502_28) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch502_40= true ;tomMatch502_29=tomMatch502_28;tomMatch502_26= tomMatch502_29.getAstName() ;tomMatch502_27= tomMatch502_29.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch502_28) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch502_40= true ;tomMatch502_30=tomMatch502_28;tomMatch502_26= tomMatch502_30.getAstName() ;tomMatch502_27= tomMatch502_30.getAstType() ;}}}if (tomMatch502_40) {if ( (tom___aName==tomMatch502_26) ) { tom.engine.adt.tomtype.types.TomType  tom___aType2=tomMatch502_27;boolean tomMatch502_39= false ;if ( (tom___aType1==tomMatch502_27) ) {if ( (tom___aType2==tomMatch502_27) ) {tomMatch502_39= true ;}}if (!(tomMatch502_39)) {           equationConstraints = addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom___aType1, tom___aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,equationConstraints);         }}}}if ( tomMatch502_end_18.isEmptyconcBQTerm() ) {tomMatch502_end_18=(( tom.engine.adt.code.types.BQTermList )varList);} else {tomMatch502_end_18= tomMatch502_end_18.getTailconcBQTerm() ;}}} while(!( (tomMatch502_end_18==(( tom.engine.adt.code.types.BQTermList )varList)) ));}}}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch502_63= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_48= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_47= null ; tom.engine.adt.tomtype.types.TomType  tomMatch502_45= null ; tom.engine.adt.tomname.types.TomName  tomMatch502_44= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch502_63= true ;tomMatch502_47=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch502_44= tomMatch502_47.getAstName() ;tomMatch502_45= tomMatch502_47.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch502_63= true ;tomMatch502_48=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch502_44= tomMatch502_48.getAstName() ;tomMatch502_45= tomMatch502_48.getAstType() ;}}}if (tomMatch502_63) { tom.engine.adt.tomtype.types.TomType  tom___aType=tomMatch502_45;if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch502_end_52=(( tom.engine.adt.tomterm.types.TomList )varPatternList);do {{ /* unamed block */if (!( tomMatch502_end_52.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch502_57= tomMatch502_end_52.getHeadconcTomTerm() ;boolean tomMatch502_62= false ; tom.engine.adt.tomtype.types.TomType  tomMatch502_56= null ; tom.engine.adt.tomname.types.TomName  tomMatch502_55= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_58= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch502_59= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch502_57) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch502_62= true ;tomMatch502_58=tomMatch502_57;tomMatch502_55= tomMatch502_58.getAstName() ;tomMatch502_56= tomMatch502_58.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch502_57) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch502_62= true ;tomMatch502_59=tomMatch502_57;tomMatch502_55= tomMatch502_59.getAstName() ;tomMatch502_56= tomMatch502_59.getAstType() ;}}}if (tomMatch502_62) {if ( (tomMatch502_44==tomMatch502_55) ) {if ( (tom___aType==tomMatch502_56) ) {



          
          addPositiveTVar(tom___aType);
        }}}}if ( tomMatch502_end_52.isEmptyconcTomTerm() ) {tomMatch502_end_52=(( tom.engine.adt.tomterm.types.TomList )varPatternList);} else {tomMatch502_end_52= tomMatch502_end_52.getTailconcTomTerm() ;}}} while(!( (tomMatch502_end_52==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));}}}}}}

  }

  
  private void checkNonLinearityOfBQVariables(BQTerm bqvar) {
    TomType newType = null;
    { /* unamed block */{ /* unamed block */if ( (bqvar instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch503_41= false ; tom.engine.adt.tomtype.types.TomType  tomMatch503_5= null ; tom.engine.adt.code.types.BQTerm  tomMatch503_8= null ; tom.engine.adt.code.types.BQTerm  tomMatch503_7= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch503_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch503_4= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch503_41= true ;tomMatch503_7=(( tom.engine.adt.code.types.BQTerm )bqvar);tomMatch503_3= tomMatch503_7.getOptions() ;tomMatch503_4= tomMatch503_7.getAstName() ;tomMatch503_5= tomMatch503_7.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch503_41= true ;tomMatch503_8=(( tom.engine.adt.code.types.BQTerm )bqvar);tomMatch503_3= tomMatch503_8.getOptions() ;tomMatch503_4= tomMatch503_8.getAstName() ;tomMatch503_5= tomMatch503_8.getAstType() ;}}}if (tomMatch503_41) { tom.engine.adt.tomoption.types.OptionList  tom___optionList=tomMatch503_3; tom.engine.adt.tomname.types.TomName  tom___aName=tomMatch503_4; tom.engine.adt.tomtype.types.TomType  tom___aType1=tomMatch503_5;if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch503_end_12=(( tom.engine.adt.code.types.BQTermList )varList);do {{ /* unamed block */if (!( tomMatch503_end_12.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch503_23= tomMatch503_end_12.getHeadconcBQTerm() ;boolean tomMatch503_38= false ; tom.engine.adt.code.types.BQTerm  tomMatch503_24= null ; tom.engine.adt.code.types.BQTerm  tomMatch503_25= null ; tom.engine.adt.tomname.types.TomName  tomMatch503_21= null ; tom.engine.adt.tomtype.types.TomType  tomMatch503_22= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch503_23) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch503_38= true ;tomMatch503_24=tomMatch503_23;tomMatch503_21= tomMatch503_24.getAstName() ;tomMatch503_22= tomMatch503_24.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch503_23) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch503_38= true ;tomMatch503_25=tomMatch503_23;tomMatch503_21= tomMatch503_25.getAstName() ;tomMatch503_22= tomMatch503_25.getAstType() ;}}}if (tomMatch503_38) {if ( (tom___aName==tomMatch503_21) ) { tom.engine.adt.tomtype.types.TomType  tom___aType2=tomMatch503_22;boolean tomMatch503_37= false ;if ( (tom___aType1==tomMatch503_22) ) {if ( (tom___aType2==tomMatch503_22) ) {tomMatch503_37= true ;}}if (!(tomMatch503_37)) {{ /* unamed block */{ /* unamed block */if ( (tom___aType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {













              if(newType == null) { /* unamed block */
                newType = getUnknownFreshTypeVar();
              }
              subtypeConstraints =
                addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType1, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints); 
              subtypeConstraints =
                addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType2, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints); 
            }}if ( (tom___aType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {               if(newType == null) { /* unamed block */                 newType = getUnknownFreshTypeVar();               }               subtypeConstraints =                 addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType1, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints);                subtypeConstraints =                 addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType2, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints);              }}}{ /* unamed block */if ( (tom___aType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( (tom___aType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch504_13= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch504_13= true ;}if (!(tomMatch504_13)) {boolean tomMatch504_12= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch504_12= true ;}if (!(tomMatch504_12)) {

              equationConstraints =
                  addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom___aType1, tom___aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,equationConstraints); 
            }}}}}}}}}}if ( tomMatch503_end_12.isEmptyconcBQTerm() ) {tomMatch503_end_12=(( tom.engine.adt.code.types.BQTermList )varList);} else {tomMatch503_end_12= tomMatch503_end_12.getTailconcBQTerm() ;}}} while(!( (tomMatch503_end_12==(( tom.engine.adt.code.types.BQTermList )varList)) ));}}if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch503_end_18=(( tom.engine.adt.tomterm.types.TomList )varPatternList);do {{ /* unamed block */if (!( tomMatch503_end_18.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch503_28= tomMatch503_end_18.getHeadconcTomTerm() ;boolean tomMatch503_40= false ; tom.engine.adt.tomname.types.TomName  tomMatch503_26= null ; tom.engine.adt.tomtype.types.TomType  tomMatch503_27= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch503_29= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch503_30= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch503_28) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch503_40= true ;tomMatch503_29=tomMatch503_28;tomMatch503_26= tomMatch503_29.getAstName() ;tomMatch503_27= tomMatch503_29.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch503_28) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch503_40= true ;tomMatch503_30=tomMatch503_28;tomMatch503_26= tomMatch503_30.getAstName() ;tomMatch503_27= tomMatch503_30.getAstType() ;}}}if (tomMatch503_40) {if ( (tom___aName==tomMatch503_26) ) { tom.engine.adt.tomtype.types.TomType  tom___aType2=tomMatch503_27;boolean tomMatch503_39= false ;if ( (tom___aType1==tomMatch503_27) ) {if ( (tom___aType2==tomMatch503_27) ) {tomMatch503_39= true ;}}if (!(tomMatch503_39)) {{ /* unamed block */{ /* unamed block */if ( (tom___aType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {               if(newType == null) { /* unamed block */                 newType = getUnknownFreshTypeVar();               }               subtypeConstraints =                 addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType1, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints);                subtypeConstraints =                 addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType2, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints);              }}if ( (tom___aType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {               if(newType == null) { /* unamed block */                 newType = getUnknownFreshTypeVar();               }               subtypeConstraints =                 addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType1, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints);                subtypeConstraints =                 addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___aType2, newType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,subtypeConstraints);              }}}{ /* unamed block */if ( (tom___aType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( (tom___aType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch505_13= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch505_13= true ;}if (!(tomMatch505_13)) {boolean tomMatch505_12= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch505_12= true ;}if (!(tomMatch505_12)) {               equationConstraints =                   addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom___aType1, tom___aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom___aName, tom___optionList) ) ,equationConstraints);              }}}}}}}}}}if ( tomMatch503_end_18.isEmptyconcTomTerm() ) {tomMatch503_end_18=(( tom.engine.adt.tomterm.types.TomList )varPatternList);} else {tomMatch503_end_18= tomMatch503_end_18.getTailconcTomTerm() ;}}} while(!( (tomMatch503_end_18==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));}}}}}}



  }

  
  private CodeList inferCodeList(CodeList cList) {
    CodeList newCList =  tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
    for(Code code : cList.getCollectionconcCode()) {
      
      code =  collectKnownTypesFromCode(code);
      
      code = inferAllTypes(code, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
      
      
      solveConstraints();
      
      code = replaceInCode(code);
      
      replaceInSymbolTable();
      newCList =  tom.engine.adt.code.types.codelist.ConsconcCode.make(code,tom_append_list_concCode(newCList, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
    }
    return newCList.reverse();
  }

  
  private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList ciList) {
    ConstraintInstructionList newCIList =  tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
    BQTermList BQTList = varList;
    for(ConstraintInstruction cInst:ciList.getCollectionconcConstraintInstruction()) {
      try {
        { /* unamed block */{ /* unamed block */if ( (cInst instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) { tom.engine.adt.tomconstraint.types.Constraint  tom___constraint= (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getConstraint() ;

            
            BQTermList globalVarList = varList;
            TomList globalVarPatternList = varPatternList;

            tom_make_TopDownCollect( new CollectVars(this) ).visitLight(tom___constraint);

            Constraint newConstraint = inferConstraint(tom___constraint);
            
            
            Instruction newAction = inferAllTypes( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getAction() , tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );

            resetVarList(globalVarPatternList);
            varPatternList = globalVarPatternList;
            newCIList =
               tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(newConstraint, newAction,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getOptions() ) ,tom_append_list_concConstraintInstruction(newCIList, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
          }}}}

      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("inferConstraintInstructionList: failure on " + cInst);
      }
    }
    varList = BQTList;
    return newCIList.reverse();
  }

  
  public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public CollectVars( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch507_4= false ; tom.engine.adt.code.types.BQTerm  tomMatch507_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch507_2= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch507_4= true ;tomMatch507_2=(( tom.engine.adt.code.types.BQTerm )tom__arg);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch507_4= true ;tomMatch507_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);}}}if (tomMatch507_4) {







 
        nkt.addBQTerm((( tom.engine.adt.code.types.BQTerm )tom__arg));
      }}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch508_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch508_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch508_2= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch508_4= true ;tomMatch508_2=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch508_4= true ;tomMatch508_3=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}}}if (tomMatch508_4) {          nkt.addTomTerm((( tom.engine.adt.tomterm.types.TomTerm )tom__arg));       }}}}return _visit_TomTerm(tom__arg,introspector);}}



  
  private Constraint inferConstraint(Constraint constraint) {
    { /* unamed block */{ /* unamed block */if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom___pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getPattern() ; tom.engine.adt.code.types.BQTerm  tom___subject= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getSubject() ; tom.engine.adt.tomtype.types.TomType  tom___aType= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getAstType() ;
 
        
        TomType tPattern = TomBase.getTermType(tom___pattern,symbolTable);
        TomType tSubject = TomBase.getTermType(tom___subject,symbolTable);
        if (tPattern == null || tPattern ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
          tPattern = getUnknownFreshTypeVar();
        }
        if (tSubject == null || tSubject ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
          tSubject = getUnknownFreshTypeVar();
        }{ /* unamed block */{ /* unamed block */if ( (tom___aType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch510_4= false ; tom.engine.adt.tomtype.types.TomType  tomMatch510_3= null ; tom.engine.adt.tomtype.types.TomType  tomMatch510_2= null ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{ /* unamed block */tomMatch510_4= true ;tomMatch510_2=(( tom.engine.adt.tomtype.types.TomType )tom___aType);}} else {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___aType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {{ /* unamed block */tomMatch510_4= true ;tomMatch510_3=(( tom.engine.adt.tomtype.types.TomType )tom___aType);}}}if (tomMatch510_4) {



            
            equationConstraints =
              addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tPattern, tom___aType, getInfoFromTomTerm(tom___pattern)) ,equationConstraints);
            subtypeConstraints = addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tPattern, tSubject, getInfoFromBQTerm(tom___subject)) ,subtypeConstraints);
          }}}}

        TomTerm newPattern = inferAllTypes(tom___pattern,tPattern);
        BQTerm newSubject = inferAllTypes(tom___subject,tSubject);
        
        return  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(newPattern, newSubject, tom___aType) ;
      }}}{ /* unamed block */if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.code.types.BQTerm  tom___left= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getLeft() ; tom.engine.adt.code.types.BQTerm  tom___right= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getRight() ;


        TomType tLeft = TomBase.getTermType(tom___left,symbolTable);
        TomType tRight = TomBase.getTermType(tom___right,symbolTable);
        if (tLeft == null || tLeft ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
          tLeft = getUnknownFreshTypeVar();
        }
        if (tRight == null || tRight ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
          tRight = getUnknownFreshTypeVar();
        }
        
        

        
        TomType lowerType = getUnknownFreshTypeVar();
        subtypeConstraints =
          addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tLeft, getInfoFromBQTerm(tom___left)) ,subtypeConstraints);
        subtypeConstraints =
          addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tRight, getInfoFromBQTerm(tom___right)) ,subtypeConstraints);
        BQTerm newLeft = inferAllTypes(tom___left,tLeft);
        BQTerm newRight = inferAllTypes(tom___right,tRight);
        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(newLeft, newRight,  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getType() ) ;
      }}}{ /* unamed block */if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
        Constraint newAConstraint =  tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
        for (Constraint cArg : cList.getCollectionconcConstraint()) { /* unamed block */
          cArg = inferConstraint(cArg);
          newAConstraint =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newAConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;
        }
        return newAConstraint;
      }}}}{ /* unamed block */if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
        Constraint newOConstraint =  tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ;
        for (Constraint cArg : cList.getCollectionconcConstraint()) { /* unamed block */
          cArg = inferConstraint(cArg);
          newOConstraint =  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(newOConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) ) ;
        }
        return newOConstraint;
      }}}}}

    return constraint;
  }

  
  private SlotList inferSlotList(SlotList sList, TomSymbol tSymbol, TomType
      contextType) {
    TomType argType = contextType;
    SlotList newSList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
    { /* unamed block */{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

        TomName argName;
        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) { /* unamed block */
          argName = slot.getSlotName();
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch512_3= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch512_3= true ;}if (!(tomMatch512_3)) {

 
                
                argType = getUnknownFreshTypeVar();
                
                
              }}}}}


          argTerm = inferAllTypes(argTerm,argType);
          newSList =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
        }
        return newSList.reverse(); 
      }}}{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch511_5= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch511_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch511_8= tomMatch511_5.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch511_9= tomMatch511_5.getCodomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch511_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch511_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch511_8.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom___headTTList= tomMatch511_8.getHeadconcTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch511_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch511_12= tomMatch511_9.getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch511_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch511_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch511_end_21=tomMatch511_12;do {{ /* unamed block */if (!( tomMatch511_end_21.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch511_end_21.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {



        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) { /* unamed block */
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


                
                argType = tomMatch511_9;
              }}}{ /* unamed block */if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch513_6= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch513_6= true ;}if (!(tomMatch513_6)) {
 
                
                
                argType = tom___headTTList;
              }}}}}

 else if ( (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName()  != argSymb.getAstName()) { /* unamed block */
            
            argType = tom___headTTList;
          } 

          
          argTerm = inferAllTypes(argTerm,argType);
          newSList =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slot.getSlotName(), argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
        }
        return newSList.reverse(); 
      }}if ( tomMatch511_end_21.isEmptyconcTypeOption() ) {tomMatch511_end_21=tomMatch511_12;} else {tomMatch511_end_21= tomMatch511_end_21.getTailconcTypeOption() ;}}} while(!( (tomMatch511_end_21==tomMatch511_12) ));}}}}}}}}{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch511_27= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch511_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch511_30= tomMatch511_27.getCodomain() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch511_30) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch511_33= tomMatch511_30.getTypeOptions() ;boolean tomMatch511_44= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch511_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch511_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch511_end_39=tomMatch511_33;do {{ /* unamed block */if (!( tomMatch511_end_39.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch511_end_39.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch511_44= true ;}}if ( tomMatch511_end_39.isEmptyconcTypeOption() ) {tomMatch511_end_39=tomMatch511_33;} else {tomMatch511_end_39= tomMatch511_end_39.getTailconcTypeOption() ;}}} while(!( (tomMatch511_end_39==tomMatch511_33) ));}if (!(tomMatch511_44)) {



        TomTerm argTerm;
        TomName argName;
        for (Slot slot : sList.getCollectionconcSlot()) { /* unamed block */
          argName = slot.getSlotName();
          argType = TomBase.getSlotType(tSymbol,argName);
          argTerm = inferAllTypes(slot.getAppl(),argType);
          newSList =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
          
        }
        return newSList.reverse(); 
      }}}}}}}

    throw new TomRuntimeException("inferSlotList: failure on " + sList);
  }

  
  private BQTermList inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType contextType) {

    if(tSymbol == null) {
      throw new TomRuntimeException("inferBQTermList: null symbol");
    }

    if(bqTList.isEmptyconcBQTerm()) {
      return  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    }

    { /* unamed block */{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

        BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) { /* unamed block */
          argTerm = inferAllTypes(argTerm, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
          newBQTList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
        }
        return newBQTList.reverse(); 
      }}}{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch514_5= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch514_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch514_8= tomMatch514_5.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch514_9= tomMatch514_5.getCodomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch514_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch514_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch514_8.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom___headTTList= tomMatch514_8.getHeadconcTomType() ; tom.engine.adt.tomtype.types.TomTypeList  tom___tailTTList= tomMatch514_8.getTailconcTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch514_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch514_12= tomMatch514_9.getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch514_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch514_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch514_end_21=tomMatch514_12;do {{ /* unamed block */if (!( tomMatch514_end_21.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch514_end_21.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {




        if(!tom___tailTTList.isEmptyconcTomType()) { /* unamed block */
          throw new TomRuntimeException("should be empty list: " + tom___tailTTList);
        }

        BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) { /* unamed block */
          TomSymbol argSymb = getSymbolFromTerm(argTerm);
          TomType argType = contextType;
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {


                
                argType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
              }}}{ /* unamed block */if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {


                
                argType = tomMatch514_9;
              }}}{ /* unamed block */if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch515_10= false ; tom.engine.adt.code.types.BQTerm  tomMatch515_8= null ; tom.engine.adt.code.types.BQTerm  tomMatch515_9= null ;if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch515_10= true ;tomMatch515_8=(( tom.engine.adt.code.types.BQTerm )argTerm);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{ /* unamed block */tomMatch515_10= true ;tomMatch515_9=(( tom.engine.adt.code.types.BQTerm )argTerm);}}}if (tomMatch515_10) {




                argType = tom___headTTList;
              }}}}}

 else if ( (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName()  != argSymb.getAstName()) { /* unamed block */
            
            argType = tom___headTTList;
          }

          
          argTerm = inferAllTypes(argTerm,argType);
          newBQTList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
        }
        return newBQTList.reverse(); 
      }}if ( tomMatch514_end_21.isEmptyconcTypeOption() ) {tomMatch514_end_21=tomMatch514_12;} else {tomMatch514_end_21= tomMatch514_end_21.getTailconcTypeOption() ;}}} while(!( (tomMatch514_end_21==tomMatch514_12) ));}}}}}}}}{ /* unamed block */if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch514_28= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom___symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch514_28) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch514_34= tomMatch514_28.getCodomain() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch514_34) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch514_37= tomMatch514_34.getTypeOptions() ; tom.engine.adt.tomslot.types.PairNameDeclList  tom___pNDList= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getPairNameDeclList() ;boolean tomMatch514_48= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch514_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch514_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch514_end_43=tomMatch514_37;do {{ /* unamed block */if (!( tomMatch514_end_43.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch514_end_43.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch514_48= true ;}}if ( tomMatch514_end_43.isEmptyconcTypeOption() ) {tomMatch514_end_43=tomMatch514_37;} else {tomMatch514_end_43= tomMatch514_end_43.getTailconcTypeOption() ;}}} while(!( (tomMatch514_end_43==tomMatch514_37) ));}if (!(tomMatch514_48)) {



        if(tom___pNDList.length() != bqTList.length()) { /* unamed block */
          Option option = TomBase.findOriginTracking( (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getOptions() );
          { /* unamed block */{ /* unamed block */if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

              TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )option).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
                  TomMessage.symbolNumberArgument,tom___symName.getString(),tom___pNDList.length(),bqTList.length());
            }}}{ /* unamed block */if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

              TomMessage.error(logger,null,0,
                  TomMessage.symbolNumberArgument,tom___symName.getString(),tom___pNDList.length(),bqTList.length());
            }}}}}

 else { /* unamed block */
          TomTypeList symDomain =  tomMatch514_28.getDomain() ;
          BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
          for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) { /* unamed block */
            TomType argType = symDomain.getHeadconcTomType();
            { /* unamed block */{ /* unamed block */if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {if (!( (( tom.engine.adt.code.types.BQTerm )argTerm).isEmptyComposite() )) {if (!(  (( tom.engine.adt.code.types.BQTerm )argTerm).getTailComposite() .isEmptyComposite() )) {

















 argType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ; }}}}}}

            argTerm = inferAllTypes(argTerm,argType);
            newBQTList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
            symDomain = symDomain.getTailconcTomType();
            
          }
          return newBQTList.reverse(); 
        }}}}}}}}


    throw new TomRuntimeException("inferBQTermList: failure on " + bqTList);
  }

  
  private void solveConstraints() {
    
    
    
    boolean errorFound = solveEquationConstraints(equationConstraints);
    if (!errorFound) {
      { /* unamed block */{ /* unamed block */if ( (subtypeConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch518_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )subtypeConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )subtypeConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )subtypeConstraints).isEmptyconcTypeConstraint() ) {tomMatch518_2= true ;}}if (!(tomMatch518_2)) {

          TypeConstraintList simplifiedConstraints = replaceInSubtypingConstraints(subtypeConstraints);
          
            solveSubtypingConstraints(simplifiedConstraints);
        }}}}

    }
    checkAllPatterns();
  }

  
  private void checkAllPatterns() {
    for (TomType pType : inputTVarList.getCollectionconcTomType()) {
      TomType sType = substitutions.get(pType);
      { /* unamed block */{ /* unamed block */if ( (sType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )sType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (equationConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch519_end_7=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);do {{ /* unamed block */if (!( tomMatch519_end_7.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch519_12= tomMatch519_end_7.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch519_12) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )sType)== tomMatch519_12.getType1() ) ) {printErrorGuessMatch( tomMatch519_12.getInfo() )


;
          }}}if ( tomMatch519_end_7.isEmptyconcTypeConstraint() ) {tomMatch519_end_7=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);} else {tomMatch519_end_7= tomMatch519_end_7.getTailconcTypeConstraint() ;}}} while(!( (tomMatch519_end_7==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) ));}}}}}{ /* unamed block */if ( (sType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )sType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (equationConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch519_end_22=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);do {{ /* unamed block */if (!( tomMatch519_end_22.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch519_27= tomMatch519_end_22.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch519_27) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )sType)== tomMatch519_27.getType2() ) ) {printErrorGuessMatch( tomMatch519_27.getInfo() )





;
          }}}if ( tomMatch519_end_22.isEmptyconcTypeConstraint() ) {tomMatch519_end_22=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);} else {tomMatch519_end_22= tomMatch519_end_22.getTailconcTypeConstraint() ;}}} while(!( (tomMatch519_end_22==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) ));}}}}}}

    }
  }

  
  private boolean solveEquationConstraints(TypeConstraintList tCList) {
    boolean errorFound = false;
    for (TypeConstraint tConstraint :
        tCList.getCollectionconcTypeConstraint()) {
matchBlockAdd :
      {
        { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch520_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch520_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ; tom.engine.adt.tomtype.types.TomType  tom___groundType1=tomMatch520_1; tom.engine.adt.tomtype.types.TomType  tom___groundType2=tomMatch520_2;boolean tomMatch520_12= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___groundType1==tomMatch520_1) ) {tomMatch520_12= true ;}}if (!(tomMatch520_12)) {boolean tomMatch520_11= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___groundType2==tomMatch520_2) ) {tomMatch520_11= true ;}}if (!(tomMatch520_11)) {if (!( (tom___groundType2==tom___groundType1) )) {



              
              
              errorFound = (errorFound || detectFail((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)));
              break matchBlockAdd;
            }}}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch520_14= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch520_15= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ; tom.engine.adt.tomtype.types.TomType  tom___groundType=tomMatch520_14;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_15) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___typeVar=tomMatch520_15;boolean tomMatch520_24= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_14) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___groundType==tomMatch520_14) ) {tomMatch520_24= true ;}}if (!(tomMatch520_24)) {



              if (substitutions.containsKey(tom___typeVar)) { /* unamed block */
                TomType mapTypeVar = substitutions.get(tom___typeVar);
                if (!isTypeVar(mapTypeVar)) { /* unamed block */
                  errorFound = (errorFound || 
                      detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom___groundType, mapTypeVar,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ));
                } else { /* unamed block */
                  
                  substitutions.addSubstitution(mapTypeVar,tom___groundType);
                }}
 else { /* unamed block */
                substitutions.addSubstitution(tom___typeVar,tom___groundType);
              }
              break matchBlockAdd;
            }}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch520_26= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch520_27= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_26) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___typeVar=tomMatch520_26; tom.engine.adt.tomtype.types.TomType  tom___groundType=tomMatch520_27;boolean tomMatch520_36= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___groundType==tomMatch520_27) ) {tomMatch520_36= true ;}}if (!(tomMatch520_36)) {



            if (substitutions.containsKey(tom___typeVar)) { /* unamed block */
              TomType mapTypeVar = substitutions.get(tom___typeVar);
              if (!isTypeVar(mapTypeVar)) { /* unamed block */
                errorFound = (errorFound || 
                    detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar, tom___groundType,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ));
              } else { /* unamed block */
                
                substitutions.addSubstitution(mapTypeVar,tom___groundType);
              }}
 else { /* unamed block */
              substitutions.addSubstitution(tom___typeVar,tom___groundType);
            }
            break matchBlockAdd;
          }}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch520_38= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch520_39= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_38) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___typeVar1=tomMatch520_38;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch520_39) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___typeVar2=tomMatch520_39;if (!( (tom___typeVar2==tom___typeVar1) )) {




              TomType mapTypeVar1;
              TomType mapTypeVar2;
              if (substitutions.containsKey(tom___typeVar1) && substitutions.containsKey(tom___typeVar2)) { /* unamed block */
                mapTypeVar1 = substitutions.get(tom___typeVar1);
                mapTypeVar2 = substitutions.get(tom___typeVar2);
                if (isTypeVar(mapTypeVar1)) { /* unamed block */
                  substitutions.addSubstitution(mapTypeVar1,mapTypeVar2);
                } else { /* unamed block */
                  if (isTypeVar(mapTypeVar2)) { /* unamed block */
                    substitutions.addSubstitution(mapTypeVar2,mapTypeVar1);
                  } else { /* unamed block */
                    errorFound = (errorFound || 
                        detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar1, mapTypeVar2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ));
                  }}

                break matchBlockAdd;
              } else if (substitutions.containsKey(tom___typeVar1)) { /* unamed block */
                mapTypeVar1 = substitutions.get(tom___typeVar1);
                substitutions.addSubstitution(tom___typeVar2,mapTypeVar1);
                break matchBlockAdd;
              } else if (substitutions.containsKey(tom___typeVar2)){ /* unamed block */
                mapTypeVar2 = substitutions.get(tom___typeVar2);
                substitutions.addSubstitution(tom___typeVar1,mapTypeVar2);
                break matchBlockAdd;
              } else { /* unamed block */
                substitutions.addSubstitution(tom___typeVar1,tom___typeVar2);
                break matchBlockAdd;
              }}}}}}}}


      }
    }
    return errorFound;
  }

  
  private boolean detectFail(TypeConstraint tConstraint) {
    { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch521_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch521_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch521_1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___tName1= tomMatch521_1.getTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch521_2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tomMatch521_8= tomMatch521_2.getTomType() ; String  tom___tName2=tomMatch521_8;boolean tomMatch521_13= false ;if ( tom___tName1.equals(tomMatch521_8) ) {if ( tom___tName2.equals(tomMatch521_8) ) {tomMatch521_13= true ;}}if (!(tomMatch521_13)) {if (!( "unknown type".equals(tom___tName1) )) {if (!( "unknown type".equals(tom___tName2) )) {



          printErrorIncompatibility(tConstraint);
          return true;
        }}}}}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch521_17= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch521_18= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch521_17) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions1= tomMatch521_17.getTypeOptions() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch521_18) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch521_25= tomMatch521_18.getTypeOptions() ; tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions2=tomMatch521_25;if (  tomMatch521_17.getTomType() .equals( tomMatch521_18.getTomType() ) ) {if ( (tom___tOptions1 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch521_end_32=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1);do {{ /* unamed block */if (!( tomMatch521_end_32.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch521_43= tomMatch521_end_32.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch521_43) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (tom___tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch521_end_38=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);do {{ /* unamed block */if (!( tomMatch521_end_38.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch521_46= tomMatch521_end_38.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch521_46) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) { tom.engine.adt.tomname.types.TomName  tomMatch521_45= tomMatch521_46.getRootSymbolName() ;boolean tomMatch521_53= false ;if ( ( tomMatch521_43.getRootSymbolName() ==tomMatch521_45) ) {if ( (tomMatch521_45==tomMatch521_45) ) {tomMatch521_53= true ;}}if (!(tomMatch521_53)) {boolean tomMatch521_52= false ;if ( (tom___tOptions1==tomMatch521_25) ) {if ( (tom___tOptions2==tomMatch521_25) ) {tomMatch521_52= true ;}}if (!(tomMatch521_52)) {






          printErrorIncompatibility(tConstraint);
          return true;
        }}}}if ( tomMatch521_end_38.isEmptyconcTypeOption() ) {tomMatch521_end_38=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);} else {tomMatch521_end_38= tomMatch521_end_38.getTailconcTypeOption() ;}}} while(!( (tomMatch521_end_38==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2)) ));}}}}if ( tomMatch521_end_32.isEmptyconcTypeOption() ) {tomMatch521_end_32=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1);} else {tomMatch521_end_32= tomMatch521_end_32.getTailconcTypeOption() ;}}} while(!( (tomMatch521_end_32==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1)) ));}}}}}}}}{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch521_56= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ; tom.engine.adt.tomtype.types.TomType  tom___t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tom___t2=tomMatch521_56;boolean tomMatch521_61= false ;if ( (tom___t1==tomMatch521_56) ) {if ( (tom___t2==tomMatch521_56) ) {tomMatch521_61= true ;}}if (!(tomMatch521_61)) {


        if (!isSubtypeOf(tom___t1,tom___t2)) { /* unamed block */
          printErrorIncompatibility(tConstraint);
          return true;
        }}}}}}


    return false;
  }

  
  private TypeConstraintList replaceInSubtypingConstraints(TypeConstraintList tCList) {
    TypeConstraintList replacedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    TomType mapT1;
    TomType mapT2;
    for (TypeConstraint tConstraint: tCList.getCollectionconcTypeConstraint()) {
      { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom___t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tom___t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;

          mapT1 = substitutions.get(tom___t1);
          mapT2 = substitutions.get(tom___t2); 
          if (mapT1 == null) { /* unamed block */
            mapT1 = tom___t1;
          }
          if (mapT2 == null) { /* unamed block */
            mapT2 = tom___t2;
          }
          if (mapT1 != mapT2) { /* unamed block */
            replacedtCList = addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(mapT1, mapT2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ,replacedtCList);
          }}}}}



    }
    return replacedtCList;
  }


  
  private void solveSubtypingConstraints(TypeConstraintList tCList) {
    TypeConstraintList solvedConstraints = tCList;
    TypeConstraintList simplifiedConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
tryBlock:
    {
      try {
        solvedConstraints = tom_make_RepeatId( new simplificationAndClosure(this) ).visitLight(solvedConstraints);
        
        
        
        while (solvedConstraints != simplifiedConstraints) {
          simplifiedConstraints = incompatibilityDetection(solvedConstraints);
          
          { /* unamed block */{ /* unamed block */if ( (simplifiedConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch523_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints);do {{ /* unamed block */if (!( tomMatch523_end_4.isEmptyconcTypeConstraint() )) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint ) tomMatch523_end_4.getHeadconcTypeConstraint() ) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint) ) {

              
              break tryBlock;
            }}if ( tomMatch523_end_4.isEmptyconcTypeConstraint() ) {tomMatch523_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints);} else {tomMatch523_end_4= tomMatch523_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch523_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints)) ));}}}}


          TypeConstraintList tmpsimplifiedConstraints = simplifiedConstraints;
          
          simplifiedConstraints = computeCanonization(simplifiedConstraints);
          
          

          
          
          

          
            
            
            
            
          

          solvedConstraints = generateSolutions(simplifiedConstraints);
          
        }
        garbageCollection(solvedConstraints);
        
        
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("solveSubtypingConstraints: failure on " +
            solvedConstraints);
      }
    }
  }

  
  public static class simplificationAndClosure extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public simplificationAndClosure( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{ /* unamed block */if (!( tomMatch524_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_14= tomMatch524_end_4.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom___t1= tomMatch524_14.getType1() ; tom.engine.adt.tomtype.types.TomType  tom___t2= tomMatch524_14.getType2() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_5= tomMatch524_end_4.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_8=tomMatch524_5;do {{ /* unamed block */if (!( tomMatch524_end_8.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_18= tomMatch524_end_8.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_18) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom___t2== tomMatch524_18.getType1() ) ) {if ( (tom___t1== tomMatch524_18.getType2() ) ) {



        nkt.solveEquationConstraints( tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom___t1, tom___t2,  tomMatch524_14.getInfo() ) , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) );
        return tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch524_end_4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch524_5,tomMatch524_end_8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch524_end_8.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
      }}}}if ( tomMatch524_end_8.isEmptyconcTypeConstraint() ) {tomMatch524_end_8=tomMatch524_5;} else {tomMatch524_end_8= tomMatch524_end_8.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_8==tomMatch524_5) ));}}if ( tomMatch524_end_4.isEmptyconcTypeConstraint() ) {tomMatch524_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch524_end_4= tomMatch524_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom___tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg); tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{ /* unamed block */if (!( tomMatch524_end_27.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_36= tomMatch524_end_27.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_36) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch524_35= tomMatch524_36.getType2() ; tom.engine.adt.tomtype.types.TomType  tom___t1= tomMatch524_36.getType1() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch524_35) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_28= tomMatch524_end_27.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_31=tomMatch524_28;do {{ /* unamed block */if (!( tomMatch524_end_31.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_43= tomMatch524_end_31.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_43) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tomMatch524_35== tomMatch524_43.getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom___t2= tomMatch524_43.getType2() ;if ( (tom___tcl instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch524_58= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl);do {{ /* unamed block */if (!( tomMatch524_end_48.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_54= tomMatch524_end_48.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_54) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom___t1== tomMatch524_54.getType1() ) ) {if ( (tom___t2== tomMatch524_54.getType2() ) ) {tomMatch524_58= true ;}}}}if ( tomMatch524_end_48.isEmptyconcTypeConstraint() ) {tomMatch524_end_48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl);} else {tomMatch524_end_48= tomMatch524_end_48.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_48==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl)) ));}if (!(tomMatch524_58)) {








          
          return
            nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___t1, tom___t2,  tomMatch524_43.getInfo() ) ,tom___tcl);
        }}}}}if ( tomMatch524_end_31.isEmptyconcTypeConstraint() ) {tomMatch524_end_31=tomMatch524_28;} else {tomMatch524_end_31= tomMatch524_end_31.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_31==tomMatch524_28) ));}}}if ( tomMatch524_end_27.isEmptyconcTypeConstraint() ) {tomMatch524_end_27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch524_end_27= tomMatch524_end_27.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_27==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom___tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg); tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{ /* unamed block */if (!( tomMatch524_end_64.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_74= tomMatch524_end_64.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_74) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch524_71= tomMatch524_74.getType1() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch524_71) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___t2= tomMatch524_74.getType2() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_65= tomMatch524_end_64.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_68=tomMatch524_65;do {{ /* unamed block */if (!( tomMatch524_end_68.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_80= tomMatch524_end_68.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_80) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom___t1= tomMatch524_80.getType1() ;if ( (tomMatch524_71== tomMatch524_80.getType2() ) ) {if ( (tom___tcl instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch524_95= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch524_end_85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl);do {{ /* unamed block */if (!( tomMatch524_end_85.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch524_91= tomMatch524_end_85.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch524_91) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom___t1== tomMatch524_91.getType1() ) ) {if ( (tom___t2== tomMatch524_91.getType2() ) ) {tomMatch524_95= true ;}}}}if ( tomMatch524_end_85.isEmptyconcTypeConstraint() ) {tomMatch524_end_85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl);} else {tomMatch524_end_85= tomMatch524_end_85.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_85==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom___tcl)) ));}if (!(tomMatch524_95)) {


          
          return
            nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___t1, tom___t2,  tomMatch524_74.getInfo() ) ,tom___tcl);
        }}}}}if ( tomMatch524_end_68.isEmptyconcTypeConstraint() ) {tomMatch524_end_68=tomMatch524_65;} else {tomMatch524_end_68= tomMatch524_end_68.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_68==tomMatch524_65) ));}}}if ( tomMatch524_end_64.isEmptyconcTypeConstraint() ) {tomMatch524_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch524_end_64= tomMatch524_end_64.getTailconcTypeConstraint() ;}}} while(!( (tomMatch524_end_64==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}}return _visit_TypeConstraintList(tom__arg,introspector);}}



  
  private TypeConstraintList incompatibilityDetection(TypeConstraintList tCList) {
    
    boolean errorFound = false;
    TypeConstraintList simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    for(TypeConstraint tConstraint : tCList.getCollectionconcTypeConstraint()) {
matchBlock :
      {
        { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {boolean tomMatch525_10= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch525_10= true ;}if (!(tomMatch525_10)) {boolean tomMatch525_9= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch525_9= true ;}if (!(tomMatch525_9)) {

            
            errorFound = (errorFound || detectFail(tConstraint));
            break matchBlock;
          }}}}}}




        simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
      }
    }
    if (errorFound) { 
      simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
    }
    return simplifiedtCList;
  }

  

  private TypeConstraintList computeCanonization(TypeConstraintList subject) {
    if(subject.isEmptyconcTypeConstraint()) {
      return subject;
    }
    List<TypeConstraint> list = new ArrayList<TypeConstraint>();
    
    for(TypeConstraint tc: ((tom.engine.adt.typeconstraints.types.typeconstraintlist.concTypeConstraint)subject)) {
      list.add(tc);
    }

    Collections.sort(list,new ConstraintComparator());
    

    boolean finished = false;
    int index = 0;
    while(!finished) {
      if(list.size()>=index+2) {
        TypeConstraint first = list.get(index);
        TypeConstraint second = list.get(index+1);

        { /* unamed block */{ /* unamed block */if ( (first instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )first) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch526_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch526_3= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch526_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___tVar=tomMatch526_2; tom.engine.adt.tomtype.types.TomType  tom___t1=tomMatch526_3; tom.engine.adt.typeconstraints.types.Info  tom___info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getInfo() ;if ( (second instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )second) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch526_10= (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType2() ;if ( (tom___tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom___t2=tomMatch526_10;boolean tomMatch526_21= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch526_10) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t2==tomMatch526_10) ) {tomMatch526_21= true ;}}if (!(tomMatch526_21)) {boolean tomMatch526_20= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch526_3) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t1==tomMatch526_3) ) {tomMatch526_20= true ;}}if (!(tomMatch526_20)) {


            TomType lowerType = minType(tom___t1,tom___t2);
            if (lowerType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
              printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___t1, tom___t2, tom___info) );
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() );
              
            } else { /* unamed block */
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___tVar, lowerType, tom___info) );
              
            }}}}}}}}}}{ /* unamed block */if ( (first instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )first) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch526_24= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch526_25= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType2() ; tom.engine.adt.tomtype.types.TomType  tom___t1=tomMatch526_24;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch526_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___tVar=tomMatch526_25; tom.engine.adt.typeconstraints.types.Info  tom___info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getInfo() ;if ( (second instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )second) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch526_31= (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType1() ; tom.engine.adt.tomtype.types.TomType  tom___t2=tomMatch526_31;if ( (tom___tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType2() ) ) {boolean tomMatch526_43= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch526_31) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t2==tomMatch526_31) ) {tomMatch526_43= true ;}}if (!(tomMatch526_43)) {boolean tomMatch526_42= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch526_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t1==tomMatch526_24) ) {tomMatch526_42= true ;}}if (!(tomMatch526_42)) {




            TomType supType = supType(tom___t1,tom___t2);
            if (supType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
              printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___t1, tom___t2, tom___info) );
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() );
              
            } else { /* unamed block */
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(supType, tom___tVar, tom___info) );
              
            }}}}}}}}}}}




        if(first == list.get(index)) {
          if(index+1<list.size() && second == list.get(index+1)) {
            index++;
            
          }
        }

      } else { 
        finished = true;
      }

    }

    TypeConstraintList result =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    for(TypeConstraint tc: list) {
      result =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tc,tom_append_list_concTypeConstraint(result, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
    }
    return result;
  }

  private class ConstraintComparator implements Comparator {

    public int compare(Object o1,Object o2) {
      TypeConstraint t1 = (TypeConstraint)o1;
      TypeConstraint t2 = (TypeConstraint)o2;
      int result = 0;
block: {
      { /* unamed block */{ /* unamed block */if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch527_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch527_3= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___st1=tomMatch527_3;if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch527_10= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch527_11= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_10) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___st2=tomMatch527_11;boolean tomMatch527_25= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_11) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___st2==tomMatch527_11) ) {tomMatch527_25= true ;}}if (!(tomMatch527_25)) {boolean tomMatch527_24= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_3) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___st1==tomMatch527_3) ) {tomMatch527_24= true ;}}if (!(tomMatch527_24)) {


            int res =  tomMatch527_2.getTomType() .compareTo( tomMatch527_10.getTomType() );
            if(res==0) { /* unamed block */
              result =  tomMatch527_10.getIndex() - tomMatch527_2.getIndex() ;
              if(result==0) { /* unamed block */
                result = tom___st1.compareToLPO(tom___st2);
              }
              break block;
            } else { /* unamed block */
              result = res;
              break block;
            }}}}}}}}}}{ /* unamed block */if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch527_28= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch527_29= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ; tom.engine.adt.tomtype.types.TomType  tom___st1=tomMatch527_28;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_29) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch527_36= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch527_37= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ; tom.engine.adt.tomtype.types.TomType  tom___st2=tomMatch527_36;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_37) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch527_51= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_28) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___st1==tomMatch527_28) ) {tomMatch527_51= true ;}}if (!(tomMatch527_51)) {boolean tomMatch527_50= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch527_36) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___st2==tomMatch527_36) ) {tomMatch527_50= true ;}}if (!(tomMatch527_50)) {




            int res =  tomMatch527_29.getTomType() .compareTo( tomMatch527_37.getTomType() );
            if(res==0) { /* unamed block */
              result =  tomMatch527_37.getIndex() - tomMatch527_29.getIndex() ;
              if(result==0) { /* unamed block */
                result = tom___st1.compareToLPO(tom___st2);
              }
              break block;
            } else { /* unamed block */
              result = res;
              break block;
            }}}}}}}}}}{ /* unamed block */if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch527_70= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch527_70= true ;}if (!(tomMatch527_70)) {




            result = -1;
            break block;
          }}}}}}}}}{ /* unamed block */if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch527_89= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch527_89= true ;}if (!(tomMatch527_89)) {


            result = +1;
            break block;
          }}}}}}}}}{ /* unamed block */if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {boolean tomMatch527_107= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch527_107= true ;}if (!(tomMatch527_107)) {boolean tomMatch527_106= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch527_106= true ;}if (!(tomMatch527_106)) {



            result = -1;
            break block;
          }}}}}}}}{ /* unamed block */if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch527_125= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch527_125= true ;}if (!(tomMatch527_125)) {boolean tomMatch527_124= false ;if ( ((( tom.engine.adt.tomtype.types.TomType ) (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch527_124= true ;}if (!(tomMatch527_124)) {


            result = +1;
            break block;
          }}}}}}}}}

      result = t1.compareToLPO(t2);
       } 

       
       return result;

    }
  }

  
  public static class applyCanonization extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public applyCanonization( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch528_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{ /* unamed block */ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom___tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch528_end_4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch528_end_4.isEmptyconcTypeConstraint() )) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint ) tomMatch528_end_4.getHeadconcTypeConstraint() ) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.typeconstraints.types.TypeConstraint  tom___c1= tomMatch528_end_4.getHeadconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch528_5= tomMatch528_end_4.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch528_end_8=tomMatch528_5;do {{ /* unamed block */ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom___tcl2=tom_get_slice_concTypeConstraint(tomMatch528_5,tomMatch528_end_8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch528_end_8.isEmptyconcTypeConstraint() )) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint ) tomMatch528_end_8.getHeadconcTypeConstraint() ) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.typeconstraints.types.TypeConstraint  tom___c2= tomMatch528_end_8.getHeadconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tom___tcl3= tomMatch528_end_8.getTailconcTypeConstraint() ;{ /* unamed block */{ /* unamed block */if ( (tom___c1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch529_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch529_3= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1).getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch529_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___tVar=tomMatch529_2; tom.engine.adt.tomtype.types.TomType  tom___t1=tomMatch529_3; tom.engine.adt.typeconstraints.types.Info  tom___info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1).getInfo() ;if ( (tom___c2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch529_10= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c2).getType2() ;if ( (tom___tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c2).getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom___t2=tomMatch529_10;boolean tomMatch529_21= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch529_10) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t2==tomMatch529_10) ) {tomMatch529_21= true ;}}if (!(tomMatch529_21)) {boolean tomMatch529_20= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch529_3) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t1==tomMatch529_3) ) {tomMatch529_20= true ;}}if (!(tomMatch529_20)) {







            TomType lowerType = nkt.minType(tom___t1,tom___t2);

            if (lowerType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
              nkt.printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___t1, tom___t2, tom___info) );
              return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom___tcl1,tom_append_list_concTypeConstraint(tom___tcl2,tom_append_list_concTypeConstraint(tom___tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
            }

            return nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___tVar, lowerType, tom___info) ,tom_append_list_concTypeConstraint(tom___tcl1,tom_append_list_concTypeConstraint(tom___tcl2,tom_append_list_concTypeConstraint(tom___tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
          }}}}}}}}}{ /* unamed block */if ( (tom___c1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch529_24= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch529_25= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1).getType2() ; tom.engine.adt.tomtype.types.TomType  tom___t1=tomMatch529_24;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch529_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___tVar=tomMatch529_25; tom.engine.adt.typeconstraints.types.Info  tom___info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c1).getInfo() ;if ( (tom___c2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c2) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch529_31= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c2).getType1() ; tom.engine.adt.tomtype.types.TomType  tom___t2=tomMatch529_31;if ( (tom___tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom___c2).getType2() ) ) {boolean tomMatch529_43= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch529_31) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t2==tomMatch529_31) ) {tomMatch529_43= true ;}}if (!(tomMatch529_43)) {boolean tomMatch529_42= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch529_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___t1==tomMatch529_24) ) {tomMatch529_42= true ;}}if (!(tomMatch529_42)) {


            TomType supType = nkt.supType(tom___t1,tom___t2);

            if (supType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) { /* unamed block */
              nkt.printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom___t1, tom___t2, tom___info) );
              return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom___tcl1,tom_append_list_concTypeConstraint(tom___tcl2,tom_append_list_concTypeConstraint(tom___tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
            }

            return nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(supType, tom___tVar, tom___info) ,tom_append_list_concTypeConstraint(tom___tcl1,tom_append_list_concTypeConstraint(tom___tcl2,tom_append_list_concTypeConstraint(tom___tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
          }}}}}}}}}}}}if ( tomMatch528_end_8.isEmptyconcTypeConstraint() ) {tomMatch528_end_8=tomMatch528_5;} else {tomMatch528_end_8= tomMatch528_end_8.getTailconcTypeConstraint() ;}}} while(!( (tomMatch528_end_8==tomMatch528_5) ));}}if ( tomMatch528_end_4.isEmptyconcTypeConstraint() ) {tomMatch528_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch528_end_4= tomMatch528_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch528_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}}return _visit_TypeConstraintList(tom__arg,introspector);}}






  
  private TomType minType(TomType t1, TomType t2) {
    
    
    if (isSubtypeOf(t1,t2)) { return t1; } 
    if (isSubtypeOf(t2,t1)) { return t2; }
    
    return  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
  }

  
  private boolean isSubtypeOf(TomType t1, TomType t2) {
    
    { /* unamed block */{ /* unamed block */if ( (t1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions1= (( tom.engine.adt.tomtype.types.TomType )t1).getTypeOptions() ; String  tom___tName1= (( tom.engine.adt.tomtype.types.TomType )t1).getTomType() ;if ( (t2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom___tOptions2= (( tom.engine.adt.tomtype.types.TomType )t2).getTypeOptions() ; String  tom___tName2= (( tom.engine.adt.tomtype.types.TomType )t2).getTomType() ;


        TomTypeList supTypet1 = dependencies.get(tom___tName1);
        { /* unamed block */{ /* unamed block */if ( (tom___tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {boolean tomMatch531_9= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch531_end_4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);do {{ /* unamed block */if (!( tomMatch531_end_4.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch531_end_4.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch531_9= true ;}}if ( tomMatch531_end_4.isEmptyconcTypeOption() ) {tomMatch531_end_4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);} else {tomMatch531_end_4= tomMatch531_end_4.getTailconcTypeOption() ;}}} while(!( (tomMatch531_end_4==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2)) ));}if (!(tomMatch531_9)) {if ( tom___tName2.equals(tom___tName1) ) {


              
              return true; 
            }}}}{ /* unamed block */if ( (tom___tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (supTypet1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypet1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypet1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch531_end_15=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);do {{ /* unamed block */if (!( tomMatch531_end_15.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch531_19= tomMatch531_end_15.getHeadconcTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch531_19) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {boolean tomMatch531_29= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch531_end_24=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);do {{ /* unamed block */if (!( tomMatch531_end_24.isEmptyconcTypeOption() )) {if ( ((( tom.engine.adt.tomtype.types.TypeOption ) tomMatch531_end_24.getHeadconcTypeOption() ) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch531_29= true ;}}if ( tomMatch531_end_24.isEmptyconcTypeOption() ) {tomMatch531_end_24=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);} else {tomMatch531_end_24= tomMatch531_end_24.getTailconcTypeOption() ;}}} while(!( (tomMatch531_end_24==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2)) ));}if (!(tomMatch531_29)) {if (  tomMatch531_19.getTomType() .equals(tom___tName2) ) {

              
              return true; 
            }}}}if ( tomMatch531_end_15.isEmptyconcTomType() ) {tomMatch531_end_15=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);} else {tomMatch531_end_15= tomMatch531_end_15.getTailconcTomType() ;}}} while(!( (tomMatch531_end_15==(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) ));}}}}{ /* unamed block */if ( (tom___tOptions1 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch531_end_35=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1);do {{ /* unamed block */if (!( tomMatch531_end_35.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch531_45= tomMatch531_end_35.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch531_45) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (tom___tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch531_end_41=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);do {{ /* unamed block */if (!( tomMatch531_end_41.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch531_48= tomMatch531_end_41.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch531_48) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( ( tomMatch531_45.getRootSymbolName() == tomMatch531_48.getRootSymbolName() ) ) {if ( tom___tName2.equals(tom___tName1) ) {



              
              return true; 
            }}}}if ( tomMatch531_end_41.isEmptyconcTypeOption() ) {tomMatch531_end_41=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);} else {tomMatch531_end_41= tomMatch531_end_41.getTailconcTypeOption() ;}}} while(!( (tomMatch531_end_41==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2)) ));}}}}if ( tomMatch531_end_35.isEmptyconcTypeOption() ) {tomMatch531_end_35=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1);} else {tomMatch531_end_35= tomMatch531_end_35.getTailconcTypeOption() ;}}} while(!( (tomMatch531_end_35==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1)) ));}}}{ /* unamed block */if ( (tom___tOptions1 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch531_end_57=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1);do {{ /* unamed block */if (!( tomMatch531_end_57.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch531_73= tomMatch531_end_57.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch531_73) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (tom___tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch531_end_63=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);do {{ /* unamed block */if (!( tomMatch531_end_63.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch531_76= tomMatch531_end_63.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch531_76) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( ( tomMatch531_73.getRootSymbolName() == tomMatch531_76.getRootSymbolName() ) ) {if ( (supTypet1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypet1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypet1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch531_end_69=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);do {{ /* unamed block */if (!( tomMatch531_end_69.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch531_79= tomMatch531_end_69.getHeadconcTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch531_79) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  tomMatch531_79.getTomType() .equals(tom___tName2) ) {




              
              return true; 
            }}}if ( tomMatch531_end_69.isEmptyconcTomType() ) {tomMatch531_end_69=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);} else {tomMatch531_end_69= tomMatch531_end_69.getTailconcTomType() ;}}} while(!( (tomMatch531_end_69==(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) ));}}}}}if ( tomMatch531_end_63.isEmptyconcTypeOption() ) {tomMatch531_end_63=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2);} else {tomMatch531_end_63= tomMatch531_end_63.getTailconcTypeOption() ;}}} while(!( (tomMatch531_end_63==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions2)) ));}}}}if ( tomMatch531_end_57.isEmptyconcTypeOption() ) {tomMatch531_end_57=(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1);} else {tomMatch531_end_57= tomMatch531_end_57.getTailconcTypeOption() ;}}} while(!( (tomMatch531_end_57==(( tom.engine.adt.tomtype.types.TypeOptionList )tom___tOptions1)) ));}}}}

        return false;
      }}}}}}

    
    return false;
  }

  
  private TomType supType(TomType t1, TomType t2) {
    
    if (isSubtypeOf(t1,t2)) { return t2; } 
    if (isSubtypeOf(t2,t1)) { return t1; }
    TomTypeList supTypes1 = dependencies.get(t1.getTomType());
    TomTypeList supTypes2 = dependencies.get(t2.getTomType());
    { /* unamed block */{ /* unamed block */if ( (t1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch532_2= (( tom.engine.adt.tomtype.types.TomType )t1).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch532_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch532_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch532_end_13=tomMatch532_2;do {{ /* unamed block */if (!( tomMatch532_end_13.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch532_24= tomMatch532_end_13.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch532_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) { String  tom___tName= (( tom.engine.adt.tomtype.types.TomType )t1).getTomType() ;if ( (t2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch532_6= (( tom.engine.adt.tomtype.types.TomType )t2).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch532_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch532_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch532_end_19=tomMatch532_6;do {{ /* unamed block */if (!( tomMatch532_end_19.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch532_27= tomMatch532_end_19.getHeadconcTypeOption() ;if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch532_27) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( tom___tName.equals( (( tom.engine.adt.tomtype.types.TomType )t2).getTomType() ) ) {boolean tomMatch532_30= false ;if ( ( tomMatch532_24.getRootSymbolName() == tomMatch532_27.getRootSymbolName() ) ) {tomMatch532_30= true ;}if (!(tomMatch532_30)) {




          
          
          return symbolTable.getType(tom___tName); 
        }}}}if ( tomMatch532_end_19.isEmptyconcTypeOption() ) {tomMatch532_end_19=tomMatch532_6;} else {tomMatch532_end_19= tomMatch532_end_19.getTailconcTypeOption() ;}}} while(!( (tomMatch532_end_19==tomMatch532_6) ));}}}}}if ( tomMatch532_end_13.isEmptyconcTypeOption() ) {tomMatch532_end_13=tomMatch532_2;} else {tomMatch532_end_13= tomMatch532_end_13.getTailconcTypeOption() ;}}} while(!( (tomMatch532_end_13==tomMatch532_2) ));}}}}{ /* unamed block */if ( (supTypes1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (supTypes2 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {boolean tomMatch532_36= false ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if ( (( tom.engine.adt.tomtype.types.TomTypeList )supTypes1).isEmptyconcTomType() ) {tomMatch532_36= true ;}}if (!(tomMatch532_36)) {boolean tomMatch532_35= false ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if ( (( tom.engine.adt.tomtype.types.TomTypeList )supTypes2).isEmptyconcTomType() ) {tomMatch532_35= true ;}}if (!(tomMatch532_35)) {



        int st1Size = supTypes1.length();
        int st2Size = supTypes2.length();

        int currentIntersectionSize = -1;
        int commonTypeIntersectionSize = -1;
        TomType lowestCommonType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
        
        for (TomType currentType:supTypes1.getCollectionconcTomType()) { /* unamed block */
          currentIntersectionSize = dependencies.get(currentType.getTomType()).length();
          if (supTypes2.getCollectionconcTomType().contains(currentType) &&
              (currentIntersectionSize > commonTypeIntersectionSize)) { /* unamed block */
            commonTypeIntersectionSize = currentIntersectionSize;
            lowestCommonType = currentType;
          }}

        return lowestCommonType;
      }}}}}}

    
    return  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
  }

  
   private TypeConstraintList generateSolutions(TypeConstraintList tCList) {
     TypeConstraintList newtCList = tCList;
matchBlockSolve :
     {
       { /* unamed block */{ /* unamed block */if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch533_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch533_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch533_9= tomMatch533_end_4.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch533_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch533_7= tomMatch533_9.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch533_8= tomMatch533_9.getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch533_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___groundType=tomMatch533_8;boolean tomMatch533_16= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch533_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___groundType==tomMatch533_8) ) {tomMatch533_16= true ;}}if (!(tomMatch533_16)) {


           
           substitutions.addSubstitution(tomMatch533_7,tom___groundType);
           newtCList = replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch533_end_4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch533_end_4.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
           break matchBlockSolve;
         }}}}if ( tomMatch533_end_4.isEmptyconcTypeConstraint() ) {tomMatch533_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch533_end_4= tomMatch533_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch533_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}{ /* unamed block */if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch533_end_21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch533_end_21.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch533_26= tomMatch533_end_21.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch533_26) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch533_24= tomMatch533_26.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch533_25= tomMatch533_26.getType2() ; tom.engine.adt.tomtype.types.TomType  tom___groundType=tomMatch533_24;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch533_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch533_33= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch533_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom___groundType==tomMatch533_24) ) {tomMatch533_33= true ;}}if (!(tomMatch533_33)) {



           substitutions.addSubstitution(tomMatch533_25,tom___groundType);
           newtCList = replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch533_end_21, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch533_end_21.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
           break matchBlockSolve;
         }}}}if ( tomMatch533_end_21.isEmptyconcTypeConstraint() ) {tomMatch533_end_21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch533_end_21= tomMatch533_end_21.getTailconcTypeConstraint() ;}}} while(!( (tomMatch533_end_21==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}

     }
     return newtCList;
   }

  private void garbageCollection(TypeConstraintList tCList) {
    TypeConstraintList newtCList = tCList;
    { /* unamed block */{ /* unamed block */if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch534_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{ /* unamed block */if (!( tomMatch534_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch534_10= tomMatch534_end_4.getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch534_10) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch534_7= tomMatch534_10.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch534_8= tomMatch534_10.getType2() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch534_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch534_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.typeconstraints.types.Info  tom___info= tomMatch534_10.getInfo() ;{ /* unamed block */{ /* unamed block */if ( (inputTVarList instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch535_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList);do {{ /* unamed block */if (!( tomMatch535_end_4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom___tVar= tomMatch535_end_4.getHeadconcTomType() ;if ( (tomMatch534_7==tom___tVar) ) {printErrorGuessMatch(tom___info)





;
            newtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
          }if ( (tomMatch534_8==tom___tVar) ) {printErrorGuessMatch(tom___info);             newtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;           }}if ( tomMatch535_end_4.isEmptyconcTomType() ) {tomMatch535_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList);} else {tomMatch535_end_4= tomMatch535_end_4.getTailconcTomType() ;}}} while(!( (tomMatch535_end_4==(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList)) ));}}}}}}}}if ( tomMatch534_end_4.isEmptyconcTypeConstraint() ) {tomMatch534_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch534_end_4= tomMatch534_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch534_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}



  }

  private void printErrorGuessMatch(Info info) {
    { /* unamed block */{ /* unamed block */if ( (info instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )info) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) { tom.engine.adt.tomname.types.TomName  tomMatch536_1= (( tom.engine.adt.typeconstraints.types.Info )info).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch536_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___termName= tomMatch536_1.getString() ;

        Option option = TomBase.findOriginTracking( (( tom.engine.adt.typeconstraints.types.Info )info).getOptions() );
        { /* unamed block */{ /* unamed block */if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

            TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )option).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
                TomMessage.cannotGuessMatchType,tom___termName); 
          }}}{ /* unamed block */if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

            TomMessage.error(logger,null,0,
                TomMessage.cannotGuessMatchType,tom___termName); 
          }}}}}}}}}



  }

  
  private boolean isTypeVar(TomType type) {
    { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 return true; }}}}

    return false;
  }

  
  private void printErrorIncompatibility(TypeConstraint tConstraint) {
    { /* unamed block */{ /* unamed block */if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch539_23= false ; tom.engine.adt.tomtype.types.TomType  tomMatch539_5= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch539_9= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch539_8= null ; tom.engine.adt.tomtype.types.TomType  tomMatch539_4= null ; tom.engine.adt.typeconstraints.types.Info  tomMatch539_6= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{ /* unamed block */tomMatch539_23= true ;tomMatch539_8=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch539_4= tomMatch539_8.getType1() ;tomMatch539_5= tomMatch539_8.getType2() ;tomMatch539_6= tomMatch539_8.getInfo() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{ /* unamed block */tomMatch539_23= true ;tomMatch539_9=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch539_4= tomMatch539_9.getType1() ;tomMatch539_5= tomMatch539_9.getType2() ;tomMatch539_6= tomMatch539_9.getInfo() ;}}}if (tomMatch539_23) { tom.engine.adt.tomtype.types.TomType  tom___tType1=tomMatch539_4; tom.engine.adt.tomtype.types.TomType  tom___tType2=tomMatch539_5; tom.engine.adt.typeconstraints.types.Info  tom___info=tomMatch539_6;if ( (tom___tType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___tType1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___tName1= (( tom.engine.adt.tomtype.types.TomType )tom___tType1).getTomType() ;if ( (tom___tType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom___tType2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___tName2= (( tom.engine.adt.tomtype.types.TomType )tom___tType2).getTomType() ;if ( (tom___info instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )tom___info) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) { tom.engine.adt.tomname.types.TomName  tomMatch539_16= (( tom.engine.adt.typeconstraints.types.Info )tom___info).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch539_16) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___termName= tomMatch539_16.getString() ;





          Option option = TomBase.findOriginTracking( (( tom.engine.adt.typeconstraints.types.Info )tom___info).getOptions() );
          { /* unamed block */{ /* unamed block */if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

              TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )option).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
                  TomMessage.incompatibleTypes,tom___tName1,tom___tName2,tom___termName); 
            }}}{ /* unamed block */if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

              TomMessage.error(logger,null,0,
                  TomMessage.incompatibleTypes,tom___tName1,tom___tName2,tom___termName); 
            }}}}}}}}}}}}}}}



  }

  
  private Code replaceInCode(Code code) {
    try {
      code = tom_make_InnermostId( new replaceFreshTypeVar(this) ).visitLight(code);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInCode: failure on " + code);
    }
    return code;
  }

  
  private void replaceInSymbolTable() {
    for(String tomName:symbolTable.keySymbolIterable()) {
      TomSymbol tSymbol = getSymbolFromName(tomName);
      try {
        tSymbol = tom_make_InnermostId( new replaceFreshTypeVar(this) ).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " + tSymbol);
      }
      symbolTable.putSymbol(tomName,tSymbol);
    }
  }

  
  public static class replaceFreshTypeVar extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public replaceFreshTypeVar( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom___typeVar=(( tom.engine.adt.tomtype.types.TomType )tom__arg);


        if(nkt.substitutions.containsKey(tom___typeVar)) { /* unamed block */
          return nkt.substitutions.get(tom___typeVar);
        }}}}}return _visit_TomType(tom__arg,introspector);}}




  
  public void printGeneratedConstraints(TypeConstraintList tCList) {
    { /* unamed block */{ /* unamed block */if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch542_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() ) {tomMatch542_2= true ;}}if (!(tomMatch542_2)) {
 
        System.out.print("\n------ Type Constraints : \n {");
        printEachConstraint(tCList);
        System.out.print("}");
      }}}}

  }

  
  public void printEachConstraint(TypeConstraintList tCList) {
    { /* unamed block */{ /* unamed block */if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch543_7= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch543_7) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom___tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getTailconcTypeConstraint() ;

        System.out.print( tomMatch543_7.getType1() );
        System.out.print(" = ");
        System.out.print( tomMatch543_7.getType2() );
        if(tom___tailtCList !=  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) { /* unamed block */
          System.out.print(", "); 
          printEachConstraint(tom___tailtCList);
        }}}}}}{ /* unamed block */if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch543_16= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getHeadconcTypeConstraint() ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch543_16) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom___tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getTailconcTypeConstraint() ;



        System.out.print( tomMatch543_16.getType1() );
        System.out.print(" <: ");
        System.out.print( tomMatch543_16.getType2() );
        if(tom___tailtCList !=  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) { /* unamed block */
          System.out.print(", "); 
          printEachConstraint(tom___tailtCList);
        }}}}}}}


  }



} 
