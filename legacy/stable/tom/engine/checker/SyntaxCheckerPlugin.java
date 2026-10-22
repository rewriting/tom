


package tom.engine.checker;



import java.util.*;



import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;



import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.exception.TomRuntimeException;



import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.TomConstraintPrettyPrinter;



import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.theory.types.*;
import tom.engine.adt.code.types.*;





import tom.library.sl.*;






public class SyntaxCheckerPlugin extends TomGenericPlugin {

     private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_append_list_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList l1,  tom.engine.adt.tomsignature.types.TomVisitList  l2) {     if( l1.isEmptyconcTomVisit() ) {       return l2;     } else if( l2.isEmptyconcTomVisit() ) {       return l1;     } else if(  l1.getTailconcTomVisit() .isEmptyconcTomVisit() ) {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,tom_append_list_concTomVisit( l1.getTailconcTomVisit() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_get_slice_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList  begin,  tom.engine.adt.tomsignature.types.TomVisitList  end, tom.engine.adt.tomsignature.types.TomVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomVisit()  ||  (end== tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( begin.getHeadconcTomVisit() ,( tom.engine.adt.tomsignature.types.TomVisitList )tom_get_slice_concTomVisit( begin.getTailconcTomVisit() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_append_list_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList l1,  tom.engine.adt.tomdeclaration.types.DeclarationList  l2) {     if( l1.isEmptyconcDeclaration() ) {       return l2;     } else if( l2.isEmptyconcDeclaration() ) {       return l1;     } else if(  l1.getTailconcDeclaration() .isEmptyconcDeclaration() ) {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,l2) ;     } else {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,tom_append_list_concDeclaration( l1.getTailconcDeclaration() ,l2)) ;     }   }   private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_get_slice_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList  begin,  tom.engine.adt.tomdeclaration.types.DeclarationList  end, tom.engine.adt.tomdeclaration.types.DeclarationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcDeclaration()  ||  (end== tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( begin.getHeadconcDeclaration() ,( tom.engine.adt.tomdeclaration.types.DeclarationList )tom_get_slice_concDeclaration( begin.getTailconcDeclaration() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {     if( l1.isEmptyconcPairNameDecl() ) {       return l2;     } else if( l2.isEmptyconcPairNameDecl() ) {       return l1;     } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }      private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}








  
  protected final static int TERM_APPL               = 0;
  protected final static int APPL_DISJUNCTION        = 2;
  protected final static int RECORD_APPL             = 3;
  protected final static int RECORD_APPL_DISJUNCTION = 4;
  protected final static int VARIABLE_STAR           = 6;
  protected final static int VARIABLE                = 7;
  protected final static int UNAMED_VARIABLE         = 8;
  protected final static int UNAMED_VARIABLE_STAR    = 9;

  private Option currentTomStructureOrgTrack;

  
  public static final PlatformOptionList PLATFORM_OPTIONS =
     tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("noSyntaxCheck", "", "Do not perform syntax checking",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) 

;
  
  private final static String OPERATOR    = "Operator";
  private final static String CONSTRUCTOR = "%op";
  private final static String OP_ARRAY    = "%oparray";
  private final static String OP_LIST     = "%oplist";
  private final static String TYPE        = "Type";
  private final static String TYPE_TERM   = "%typeterm";

  
  private final static String EQUALS      = "equals";
  private final static String GET_ELEMENT = "get_element";
  private final static String GET_SIZE    = "get_size";
  private final static String GET_HEAD    = "get_head";
  private final static String GET_TAIL    = "get_tail";
  private final static String IS_EMPTY    = "is_empty";
  
  private final static String MAKE_APPEND = "make_append";
  private final static String MAKE_EMPTY  = "make_empty";
  private final static String MAKE_INSERT = "make_insert";
  private final static String MAKE        = "make";

  
  private  Collection<String> alreadyStudiedTypes =  null;
  
  private  Collection<String> alreadyStudiedSymbols =  null;

  
  private final static List<String> TypeTermSignature =
    new ArrayList<String>(Arrays.asList(new String[]{ SyntaxCheckerPlugin.EQUALS }));

  
  public SyntaxCheckerPlugin() {
    super("SyntaxCheckerPlugin");
    reinit();
  }

  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private Option getCurrentTomStructureOrgTrack() {
    return currentTomStructureOrgTrack;
  }

  private void setCurrentTomStructureOrgTrack(Option currentTomStructureOrgTrack) {
    this.currentTomStructureOrgTrack = currentTomStructureOrgTrack;
  }

  
  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS; 
  }

  protected void reinit() {
    currentTomStructureOrgTrack = null;
    alreadyStudiedTypes   = new HashSet<String>();
    alreadyStudiedSymbols = new HashSet<String>();
  }
  
  

  protected String findOriginTrackingFileName(OptionList optionList) {
    { /* unamed block */{ /* unamed block */if ( (optionList instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch119_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);do {{ /* unamed block */if (!( tomMatch119_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch119_8= tomMatch119_end_4.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch119_8) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return  tomMatch119_8.getFileName() ; }}if ( tomMatch119_end_4.isEmptyconcOption() ) {tomMatch119_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);} else {tomMatch119_end_4= tomMatch119_end_4.getTailconcOption() ;}}} while(!( (tomMatch119_end_4==(( tom.engine.adt.tomoption.types.OptionList )optionList)) ));}}}}

    return "unknown filename";
  }

  protected int findOriginTrackingLine(OptionList optionList) {
    { /* unamed block */{ /* unamed block */if ( (optionList instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch120_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);do {{ /* unamed block */if (!( tomMatch120_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch120_8= tomMatch120_end_4.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch120_8) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return  tomMatch120_8.getLine() ; }}if ( tomMatch120_end_4.isEmptyconcOption() ) {tomMatch120_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);} else {tomMatch120_end_4= tomMatch120_end_4.getTailconcOption() ;}}} while(!( (tomMatch120_end_4==(( tom.engine.adt.tomoption.types.OptionList )optionList)) ));}}}}

    return -1;
  }

  public void run(Map informationTracker) {
    
    if(isActivated()) {
      long startChrono = System.currentTimeMillis();
      try {
        
        reinit();
        
        try {
          Code code = (Code)getWorkingTerm();
          
          tom_make_TopDownCollect( new CheckSyntax(this) ).visitLight(code);
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("strategy failed");
        }
        
        TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
            TomMessage.tomSyntaxCheckingPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        TomMessage.error(getLogger(), 
            getStreamManager().getInputFileName(), 0,
            TomMessage.exceptionMessage,
            getClass().getName(), 
            getStreamManager().getInputFileName(),
            e.getMessage() );
        e.printStackTrace();
      }
    } else {
      
      TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          TomMessage.syntaxCheckerInactivated);
    }
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noSyntaxCheck");
  }

  
  public static class CheckSyntax extends tom.library.sl.AbstractStrategyBasic {private  SyntaxCheckerPlugin  scp;public CheckSyntax( SyntaxCheckerPlugin  scp) {super(( new tom.library.sl.Identity() ));this.scp=scp;}public  SyntaxCheckerPlugin  getscp() {return scp;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {







































































        scp.verifyBQAppl((( tom.engine.adt.code.types.BQTerm )tom__arg));
      }}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {                  scp.verifyMatch( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() ,  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() );       }}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.Strategy) ) { tom.engine.adt.tomsignature.types.TomVisitList  tom___list= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getVisitList() ; tom.engine.adt.tomoption.types.Option  tom___origin= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() ; tom.engine.adt.tomdeclaration.types.Declaration  tom___s=(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg);         if(tom___list.isEmptyconcTomVisit()) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tom___origin instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tom___origin) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {                TomMessage.error(scp.getLogger(),  (( tom.engine.adt.tomoption.types.Option )tom___origin).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )tom___origin).getLine() , TomMessage.emptyStrategy);               return tom___s;             }}}}           TomMessage.error(scp.getLogger(), null, -1, TomMessage.emptyStrategy);           return tom___s;         }                  scp.verifyStrategy(tom___list);         throw new tom.library.sl.VisitFailure();       }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch123_6= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch123_6) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifyTypeDecl(SyntaxCheckerPlugin.TYPE_TERM,  tomMatch123_6.getString() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getDeclarations() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() );         throw new tom.library.sl.VisitFailure();       }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch123_15= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch123_15) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifySymbol(SyntaxCheckerPlugin.CONSTRUCTOR, scp.getSymbolFromName( tomMatch123_15.getString() ));         throw new tom.library.sl.VisitFailure();       }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch123_22= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch123_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifySymbol(SyntaxCheckerPlugin.OP_ARRAY, scp.getSymbolFromName( tomMatch123_22.getString() ));         throw new tom.library.sl.VisitFailure();       }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch123_29= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch123_29) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifySymbol(SyntaxCheckerPlugin.OP_LIST, scp.getSymbolFromName( tomMatch123_29.getString() ));         throw new tom.library.sl.VisitFailure();       }}}}}return _visit_Declaration(tom__arg,introspector);}}



  
  
  
  private void verifyTypeDecl(String declType, String tomName, DeclarationList listOfDeclaration, Option typeOrgTrack) {
    setCurrentTomStructureOrgTrack(typeOrgTrack);
    
    verifyMultipleDefinition(tomName, declType, TYPE);
    
    List<String> verifyList = new ArrayList<String>(SyntaxCheckerPlugin.TypeTermSignature);


    { /* unamed block */{ /* unamed block */if ( (listOfDeclaration instanceof tom.engine.adt.tomdeclaration.types.DeclarationList) ) {if ( (((( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration) || ((( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration)) ) { tom.engine.adt.tomdeclaration.types.DeclarationList  tomMatch125_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration);do {{ /* unamed block */if (!( tomMatch125_end_4.isEmptyconcDeclaration() )) { tom.engine.adt.tomdeclaration.types.Declaration  tom___d= tomMatch125_end_4.getHeadconcDeclaration() ;
 
matchblock:{ /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch126_1= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getTermArg1() ; tom.engine.adt.code.types.BQTerm  tomMatch126_2= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getTermArg2() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch126_1) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch126_7= tomMatch126_1.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch126_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch126_2) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch126_13= tomMatch126_2.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch126_13) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.EQUALS,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() , tomMatch126_7.getString() , tomMatch126_13.getString() ,declType)



;
                 break matchblock;
               }}}}}}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {checkField(SyntaxCheckerPlugin.GET_HEAD,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() ,declType)


;
                 break matchblock;
               }}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {checkField(SyntaxCheckerPlugin.GET_TAIL,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() ,declType)

;
                 break matchblock;
               }}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {checkField(SyntaxCheckerPlugin.IS_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() ,declType)

;
                 break matchblock;
               }}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch126_32= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getVariable() ; tom.engine.adt.code.types.BQTerm  tomMatch126_33= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getIndex() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch126_32) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch126_37= tomMatch126_32.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch126_37) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch126_33) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch126_43= tomMatch126_33.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch126_43) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.GET_ELEMENT,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() , tomMatch126_37.getString() , tomMatch126_43.getString() ,declType)


;
                 break matchblock;
               }}}}}}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {checkField(SyntaxCheckerPlugin.GET_SIZE,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() ,declType)

;
                 break matchblock;
               }}}}}}if ( tomMatch125_end_4.isEmptyconcDeclaration() ) {tomMatch125_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration);} else {tomMatch125_end_4= tomMatch125_end_4.getTailconcDeclaration() ;}}} while(!( (tomMatch125_end_4==(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration)) ));}}}}





    
    if(verifyList.contains(SyntaxCheckerPlugin.EQUALS)) {
      verifyList.remove(verifyList.indexOf(SyntaxCheckerPlugin.EQUALS));
    }
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(declType, verifyList);
    }
  } 

  private void verifyMultipleDefinition(String name, String symbolType, String OperatorOrType) {
    List list;
    if(OperatorOrType.equals(SyntaxCheckerPlugin.OPERATOR)) {
      if(alreadyStudiedSymbols.contains(name)) {
        TomMessage.error(getLogger(),
            getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.multipleSymbolDefinitionError, name);
      } else {
        alreadyStudiedSymbols.add(name);
      }
    } else {
      if(alreadyStudiedTypes.contains(name)) {
        TomMessage.error(getLogger(),
            getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.multipleSortDefinitionError,
            name);
      } else {
        alreadyStudiedTypes.add(name);
      }
    }
  } 

  private void checkField(String function, List foundFunctions, Option orgTrack, String symbolType) {
    if(foundFunctions.contains(function)) {
      foundFunctions.remove(foundFunctions.indexOf(function));
    } else {
      TomMessage.error(getLogger(), orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.macroFunctionRepeated,
          function);
    }
  } 

  private  void checkFieldAndLinearArgs(String function, List foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      TomMessage.error(getLogger(), orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.nonLinearMacroFunction,
          function, name1);
    }
  } 

  
  
  
  private void verifySymbol(String symbolType, TomSymbol tomSymbol) {
    int domainLength;

    String symbStrName = tomSymbol.getAstName().getString();

    OptionList optionList = tomSymbol.getOptions();
    
    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(optionList));

    
    
    verifyMultipleDefinition(symbStrName, symbolType, SyntaxCheckerPlugin.OPERATOR);
    verifySymbolCodomain(TomBase.getSymbolCodomain(tomSymbol), symbStrName, symbolType);
    domainLength = verifySymbolDomain(TomBase.getSymbolDomain(tomSymbol), symbStrName, symbolType);
    verifySymbolMacroFunctions(optionList, domainLength, symbolType);
  } 

  private void verifySymbolCodomain(TomType codomain, String symbName, String symbolType) {
    { /* unamed block */{ /* unamed block */if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.tomtype.Codomain) ) {

        if(getSymbolTable().getSymbolFromName( (( tom.engine.adt.tomtype.types.TomType )codomain).getAstName() ) == null) { /* unamed block */
          TomMessage.error(getLogger(), 
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              symbName, codomain);
        }
        return;
      }}}{ /* unamed block */if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___typeName= (( tom.engine.adt.tomtype.types.TomType )codomain).getTomType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) (( tom.engine.adt.tomtype.types.TomType )codomain).getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {


        if(!testTypeExistence(tom___typeName)) { /* unamed block */
          TomMessage.error(getLogger(),
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              symbName, tom___typeName);
          if(tom___typeName.equals("Strategy")) { /* unamed block */
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.missingIncludeSL);
          }}

        return;
      }}}}{ /* unamed block */if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {


        TomMessage.error(getLogger(),
            getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.symbolCodomainError,
            symbName, "");
        return;
      }}}}

    throw new TomRuntimeException("Strange codomain " + codomain);
  }

  private int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
    int position = 1;
    if(symbolType.equals(SyntaxCheckerPlugin.CONSTRUCTOR)) {
      { /* unamed block */{ /* unamed block */if ( (args instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch128_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )args);do {{ /* unamed block */if (!( tomMatch128_end_4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch128_10= tomMatch128_end_4.getHeadconcTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch128_10) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___typeName= tomMatch128_10.getTomType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch128_10.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {
 
          if(!testTypeExistence(tom___typeName)) { /* unamed block */
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.symbolDomainError,
                Integer.valueOf(position), symbName, tom___typeName);
          }
          position++;
        }}}if ( tomMatch128_end_4.isEmptyconcTomType() ) {tomMatch128_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )args);} else {tomMatch128_end_4= tomMatch128_end_4.getTailconcTomType() ;}}} while(!( (tomMatch128_end_4==(( tom.engine.adt.tomtype.types.TomTypeList )args)) ));}}}}

      return (position-1);
    } else { 
      { /* unamed block */{ /* unamed block */if ( (args instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( (( tom.engine.adt.tomtype.types.TomTypeList )args).isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch129_6= (( tom.engine.adt.tomtype.types.TomTypeList )args).getHeadconcTomType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch129_6) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___typeName= tomMatch129_6.getTomType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch129_6.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {if (  (( tom.engine.adt.tomtype.types.TomTypeList )args).getTailconcTomType() .isEmptyconcTomType() ) {

          if(!testTypeExistence(tom___typeName)) { /* unamed block */
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.listSymbolDomainError,
                symbName, tom___typeName);
          }}}}}}}}}

 
      return 1;
    }
  } 

  private void verifySymbolMacroFunctions(OptionList option, int domainLength, String symbolType) {
    List<String> verifyList = new ArrayList<String>();
    boolean foundOpMake = false;
    if(symbolType.equals(SyntaxCheckerPlugin.CONSTRUCTOR)) { 
      
    } else if(symbolType == SyntaxCheckerPlugin.OP_ARRAY ) {
      verifyList.add(SyntaxCheckerPlugin.MAKE_EMPTY);
      verifyList.add(SyntaxCheckerPlugin.MAKE_APPEND);
    } else if(symbolType == SyntaxCheckerPlugin.OP_LIST) {
      verifyList.add(SyntaxCheckerPlugin.MAKE_EMPTY);
      verifyList.add(SyntaxCheckerPlugin.MAKE_INSERT);
    }


    { /* unamed block */{ /* unamed block */if ( (option instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )option) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )option) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch130_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);do {{ /* unamed block */if (!( tomMatch130_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch130_8= tomMatch130_end_4.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch130_8) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) { tom.engine.adt.tomdeclaration.types.Declaration  tom___d= tomMatch130_8.getAstDeclaration() ;
 
matchblock:{ /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {checkField(SyntaxCheckerPlugin.MAKE_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() ,symbolType)



;
                 break matchblock;
               }}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.code.types.BQTerm  tomMatch131_5= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getVarList() ; tom.engine.adt.code.types.BQTerm  tomMatch131_6= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getVarElt() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch131_5) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch131_10= tomMatch131_5.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch131_10) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch131_6) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch131_16= tomMatch131_6.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch131_16) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.MAKE_APPEND,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() , tomMatch131_10.getString() , tomMatch131_16.getString() ,symbolType)

;
                 break matchblock;
               }}}}}}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {checkField(SyntaxCheckerPlugin.MAKE_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() ,symbolType)


;
                 break matchblock;
               }}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.code.types.BQTerm  tomMatch131_27= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getVarList() ; tom.engine.adt.code.types.BQTerm  tomMatch131_28= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getVarElt() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch131_27) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch131_32= tomMatch131_27.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch131_32) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch131_28) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch131_38= tomMatch131_28.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch131_38) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.MAKE_INSERT,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() , tomMatch131_32.getString() , tomMatch131_38.getString() ,symbolType)

;
                 break matchblock;
               }}}}}}}{ /* unamed block */if ( (tom___d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom___d) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomoption.types.Option  tomMatch131_46= (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getOrgTrack() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch131_46) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {


                 if(!foundOpMake) { /* unamed block */
                   foundOpMake = true;
                   verifyMakeDeclArgs( (( tom.engine.adt.tomdeclaration.types.Declaration )tom___d).getArgs() ,domainLength,tomMatch131_46,symbolType);
                 } else { /* unamed block */
                   TomMessage.error(getLogger(),  tomMatch131_46.getFileName() ,  tomMatch131_46.getLine() , 
                       TomMessage.macroFunctionRepeated,
                       SyntaxCheckerPlugin.MAKE);
                 }
                 break matchblock;
               }}}}}}}}if ( tomMatch130_end_4.isEmptyconcOption() ) {tomMatch130_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);} else {tomMatch130_end_4= tomMatch130_end_4.getTailconcOption() ;}}} while(!( (tomMatch130_end_4==(( tom.engine.adt.tomoption.types.OptionList )option)) ));}}}}




    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(symbolType, verifyList);
    }
  }  

  private void verifyMakeDeclArgs(BQTermList argsList, int domainLength, Option orgTrack, String symbolType) {
    
    
    int nbArgs = 0;
    Collection<String> listVar = new HashSet<String>();
    { /* unamed block */{ /* unamed block */if ( (argsList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )argsList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )argsList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch132_end_4=(( tom.engine.adt.code.types.BQTermList )argsList);do {{ /* unamed block */if (!( tomMatch132_end_4.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch132_8= tomMatch132_end_4.getHeadconcBQTerm() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch132_8) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch132_7= tomMatch132_8.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch132_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___name= tomMatch132_7.getString() ;
 
        if(listVar.contains(tom___name)) { /* unamed block */
          TomMessage.error(getLogger(),
              orgTrack.getFileName(),orgTrack.getLine(),
              TomMessage.nonLinearMacroFunction,
              SyntaxCheckerPlugin.MAKE, tom___name);
        } else { /* unamed block */
          listVar.add(tom___name);
        }
        nbArgs++;
      }}}if ( tomMatch132_end_4.isEmptyconcBQTerm() ) {tomMatch132_end_4=(( tom.engine.adt.code.types.BQTermList )argsList);} else {tomMatch132_end_4= tomMatch132_end_4.getTailconcBQTerm() ;}}} while(!( (tomMatch132_end_4==(( tom.engine.adt.code.types.BQTermList )argsList)) ));}}}}

    if(nbArgs != domainLength) {
      TomMessage.error(getLogger(),
          orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.badMakeDefinition,
          Integer.valueOf(nbArgs), Integer.valueOf(domainLength));
    }
  } 

  private void verifySymbolPairNameDeclList(PairNameDeclList pairNameDeclList, String symbolType) {
    
    Collection<String> listSlot = new HashSet<String>();
    { /* unamed block */{ /* unamed block */if ( (pairNameDeclList instanceof tom.engine.adt.tomslot.types.PairNameDeclList) ) {if ( (((( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl) || ((( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl)) ) { tom.engine.adt.tomslot.types.PairNameDeclList  tomMatch133_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList);do {{ /* unamed block */if (!( tomMatch133_end_4.isEmptyconcPairNameDecl() )) { tom.engine.adt.tomslot.types.PairNameDecl  tomMatch133_8= tomMatch133_end_4.getHeadconcPairNameDecl() ;if ( ((( tom.engine.adt.tomslot.types.PairNameDecl )tomMatch133_8) instanceof tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch133_7= tomMatch133_8.getSlotName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch133_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___name= tomMatch133_7.getString() ;
 
        if(listSlot.contains(tom___name)) { /* unamed block */}


 else { /* unamed block */
          listSlot.add(tom___name);
        }}}}if ( tomMatch133_end_4.isEmptyconcPairNameDecl() ) {tomMatch133_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList);} else {tomMatch133_end_4= tomMatch133_end_4.getTailconcPairNameDecl() ;}}} while(!( (tomMatch133_end_4==(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList)) ));}}}}


  } 

  private void messageMissingMacroFunctions(String symbolType, List list) {
    StringBuilder listOfMissingMacros = new StringBuilder();
    for(int i=0;i<list.size();i++) {
      listOfMissingMacros.append(list.get(i) + ",  ");
    }
    String stringListOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
    TomMessage.error(getLogger(),
        getCurrentTomStructureOrgTrack().getFileName(),
        getCurrentTomStructureOrgTrack().getLine(),
        TomMessage.missingMacroFunctions,
        stringListOfMissingMacros);
  } 

  
  
  
  
  private void verifyMatch(ConstraintInstructionList constraintInstructionList, OptionList optionList) throws VisitFailure {

    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(optionList));
    Collection<Constraint> constraints = new HashSet<Constraint>();    
    Map<TomName, Collection<TomName>> varRelationsMap = new HashMap<TomName, Collection<TomName>>();
    Map<Constraint, Instruction> orActionMap = new HashMap<Constraint, Instruction>();
    Collection tmp = new HashSet();
    tom_make_TopDownCollect( new CollectConstraints(constraints,tmp,orActionMap) ).visitLight(constraintInstructionList);
    for(Constraint constr: constraints) {
matchLbl: { /* unamed block */{ /* unamed block */if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom___pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() ; tom.engine.adt.code.types.BQTerm  tom___subject= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ; tom.engine.adt.tomtype.types.TomType  tom___astType= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getAstType() ;

              Collection<TomName> patternVars = new HashSet<TomName>();
              Collection<TomName> subjectVars = new HashSet<TomName>();
              tom_make_TopDownCollect( new CollectVariables(patternVars) ).visitLight(tom___pattern);
              tom_make_TopDownCollect( new CollectVariables(subjectVars) ).visitLight(tom___subject);
              computeDependencies(varRelationsMap,patternVars,subjectVars);

              
              
              

              if(tom___astType == SymbolTable.TYPE_UNKNOWN) { /* unamed block */

                if (!getOptionBooleanValue("newtyper")) { /* unamed block */
                  
                  TomType typeMatch = getSubjectType(tom___subject,constraints);
                  if(typeMatch == null) { /* unamed block */{ /* unamed block */{ /* unamed block */if ( (tom___subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch135_1= (( tom.engine.adt.code.types.BQTerm )tom___subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch135_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


                        TomMessage.error(getLogger(),
                            getCurrentTomStructureOrgTrack().getFileName(),
                            getCurrentTomStructureOrgTrack().getLine(),
                            TomMessage.cannotGuessMatchType,
                             tomMatch135_1.getString() );
                        return;
                      }}}}{ /* unamed block */if ( (tom___subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch135_8= (( tom.engine.adt.code.types.BQTerm )tom___subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch135_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

                        TomMessage.error(getLogger(),
                            getCurrentTomStructureOrgTrack().getFileName(),
                            getCurrentTomStructureOrgTrack().getLine(),
                            TomMessage.cannotGuessMatchType,
                             tomMatch135_8.getString() );
                        return;
                      }}}}{ /* unamed block */if ( (tom___subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom___subject) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {if ( ((( tom.engine.adt.tomname.types.TomName ) (( tom.engine.adt.code.types.BQTerm )tom___subject).getAstName() ) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {}}}}}




                    return;
                  }}

                verifyMatchPattern(tom___pattern, getPatternType(tom___pattern));
              } else { /* unamed block */
                
                if (!testTypeExistence(tom___astType.getTomType())) { /* unamed block */
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.unknownType,
                      tom___astType.getTomType());
                }
                
                verifyMatchPattern(tom___pattern, tom___astType);
              }}}}{ /* unamed block */if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.code.types.BQTerm  tom___left= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getLeft() ; tom.engine.adt.code.types.BQTerm  tom___right= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getRight() ;{ /* unamed block */{ /* unamed block */if ( (tom___left instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch136_5= false ;boolean tomMatch136_6= false ; tom.engine.adt.code.types.BQTerm  tomMatch136_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch136_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch136_4= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom___left) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch136_6= true ;tomMatch136_2=(( tom.engine.adt.code.types.BQTerm )tom___left);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___left) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{ /* unamed block */tomMatch136_6= true ;tomMatch136_3=(( tom.engine.adt.code.types.BQTerm )tom___left);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___left) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{ /* unamed block */tomMatch136_6= true ;tomMatch136_4=(( tom.engine.adt.code.types.BQTerm )tom___left);}}}}if (tomMatch136_6) {tomMatch136_5= true ;}if (!(tomMatch136_5)) {













                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.termOrVariableNumericLeft,
                      getName(tom___left));
                  return;
                }}}}{ /* unamed block */{ /* unamed block */if ( (tom___right instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch137_5= false ;boolean tomMatch137_6= false ; tom.engine.adt.code.types.BQTerm  tomMatch137_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch137_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch137_4= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom___right) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch137_6= true ;tomMatch137_2=(( tom.engine.adt.code.types.BQTerm )tom___right);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___right) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{ /* unamed block */tomMatch137_6= true ;tomMatch137_3=(( tom.engine.adt.code.types.BQTerm )tom___right);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom___right) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{ /* unamed block */tomMatch137_6= true ;tomMatch137_4=(( tom.engine.adt.code.types.BQTerm )tom___right);}}}}if (tomMatch137_6) {tomMatch137_5= true ;}if (!(tomMatch137_5)) {




                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.termOrVariableNumericRight,
                      getName(tom___right));
                  return;
                }}}}

              
              TomType leftType = TomBase.getTermType(tom___left,getSymbolTable());
              TomType rightType = TomBase.getTermType(tom___right,getSymbolTable());
              
              if(leftType != null && leftType != SymbolTable.TYPE_UNKNOWN && leftType !=  tom.engine.adt.tomtype.types.tomtype.EmptyType.make()  
                  && rightType != null && rightType != SymbolTable.TYPE_UNKNOWN && rightType !=  tom.engine.adt.tomtype.types.tomtype.EmptyType.make()  
                  && (leftType != rightType)) { /* unamed block */
                TomMessage.error(getLogger(),
                    getCurrentTomStructureOrgTrack().getFileName(),
                    getCurrentTomStructureOrgTrack().getLine(),
                    TomMessage.invalidTypesNumeric,
                    leftType,getName(tom___left),rightType,getName(tom___right)); 
                return;
              }}}}{ /* unamed block */if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tom___oc=(( tom.engine.adt.tomconstraint.types.Constraint )constr);



              if(!verifyOrConstraint(tom___oc,orActionMap.get(tom___oc))) { /* unamed block */
                return;
              }}}}}


    } 

    checkVarDependencies(varRelationsMap);
  } 

  
  private void computeDependencies(Map<TomName, Collection<TomName>> varRelationsMap, Collection<TomName> patternVars, Collection<TomName> subjectVars) {
    for(TomName x:patternVars) {      
      if(!varRelationsMap.keySet().contains(x)) {
        varRelationsMap.put(x,subjectVars);
      } else { 
        Collection<TomName> tmp = new HashSet<TomName>(subjectVars);
        tmp.addAll(varRelationsMap.get(x));
        varRelationsMap.put(x,tmp);
      }
    }
  }

  
  private void checkVarDependencies(Map<TomName, Collection<TomName>> varRelationsMap) {
    for(TomName var:varRelationsMap.keySet()) {
      isVariablePresent(var, varRelationsMap.get(var), varRelationsMap, new HashSet<TomName>());
    }
  }

  private void isVariablePresent(TomName var, Collection<TomName> associatedList, Map<TomName, Collection<TomName>> varRelationsMap, Collection<TomName> checked) {    
    if(associatedList.contains(var)) {
      TomMessage.error(getLogger(),
          getCurrentTomStructureOrgTrack().getFileName(),
          getCurrentTomStructureOrgTrack().getLine(),
          TomMessage.circularReferences,
          ((Name)var).getString());       
    } else {
      for(TomName tn: associatedList) {
        if(checked.contains(tn)) { return; }
        checked.add(tn);
        Collection<TomName> lst = varRelationsMap.get(tn);
        if(lst != null) { 
          isVariablePresent(var, lst, varRelationsMap, checked);
        }
      }
    }    
  }

  
  private boolean verifyOrConstraint(Constraint orConstraint, Instruction action) throws VisitFailure {
    Collection<TomTerm> freeVarList1 = new HashSet<TomTerm>();
    Collection<TomTerm> freeVarList2 = new HashSet<TomTerm>();
    { /* unamed block */{ /* unamed block */if ( (orConstraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )orConstraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )orConstraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch138_end_4=(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint);do {{ /* unamed block */if (!( (  tomMatch138_end_4.isEmptyOrConstraint()  ||  (tomMatch138_end_4== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) { tom.engine.adt.tomconstraint.types.Constraint  tom___x=(( ((tomMatch138_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (tomMatch138_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( tomMatch138_end_4.getHeadOrConstraint() ):(tomMatch138_end_4));

        if(tom___x !=  tom.engine.adt.tomconstraint.types.constraint.FalseConstraint.make() ) { /* unamed block */
          
          
          
          Strategy collect =  tom.library.sl.Sequence.make(tom_make_TopDownCollect( new CollectAliasVar(freeVarList2) ), tom.library.sl.Sequence.make(tom_make_TopDownCollect( new CollectFreeVar(freeVarList2) ), null ) ) 

;

          { /* unamed block */{ /* unamed block */if ( (tom___x instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom___x) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 
              collect.visitLight( (( tom.engine.adt.tomconstraint.types.Constraint )tom___x).getPattern() );
            }}}{ /* unamed block */if ( (tom___x instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )tom___x) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )tom___x) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch139_end_10=(( tom.engine.adt.tomconstraint.types.Constraint )tom___x);do {{ /* unamed block */if (!( (  tomMatch139_end_10.isEmptyAndConstraint()  ||  (tomMatch139_end_10== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch139_16=(( ((tomMatch139_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch139_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch139_end_10.getHeadAndConstraint() ):(tomMatch139_end_10));if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch139_16) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 
              collect.visitLight( tomMatch139_16.getPattern() );
            }}if ( (  tomMatch139_end_10.isEmptyAndConstraint()  ||  (tomMatch139_end_10== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {tomMatch139_end_10=(( tom.engine.adt.tomconstraint.types.Constraint )tom___x);} else {tomMatch139_end_10=(( ((tomMatch139_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch139_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch139_end_10.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));}}} while(!( (tomMatch139_end_10==(( tom.engine.adt.tomconstraint.types.Constraint )tom___x)) ));}}}}

          
          

          if(!freeVarList1.isEmpty()) { /* unamed block */
            for(TomTerm term:freeVarList2) { /* unamed block */
              if(!containsVariable(term,freeVarList1)) { /* unamed block */
                if(containsVariable(term, action)) { /* unamed block */              
                  String varName = term.getAstName().getString();
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.freeVarNotPresentInOr,
                      varName);
                  return false;
                }}}


            for(TomTerm term:freeVarList1) { /* unamed block */
              if(!containsVariable(term,freeVarList2)) { /* unamed block */
                if(containsVariable(term, action))  { /* unamed block */
                  String varName = term.getAstName().getString();
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.freeVarNotPresentInOr,
                      varName);
                  return false;
                }}}}



          freeVarList1 = new HashSet<TomTerm>(freeVarList2);
          freeVarList2.clear();
        }}if ( (  tomMatch138_end_4.isEmptyOrConstraint()  ||  (tomMatch138_end_4== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) ) {tomMatch138_end_4=(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint);} else {tomMatch138_end_4=(( ((tomMatch138_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (tomMatch138_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( tomMatch138_end_4.getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ));}}} while(!( (tomMatch138_end_4==(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint)) ));}}}}


    return true;
  }

  private boolean containsVariable(TomTerm var, Collection<TomTerm> list) {
    for(TomTerm t:list) {
      { /* unamed block */{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( (t instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )t) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() == (( tom.engine.adt.tomterm.types.TomTerm )t).getAstName() ) ) {
 return true; }}}}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( (t instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )t) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() == (( tom.engine.adt.tomterm.types.TomTerm )t).getAstName() ) ) {
 return true; }}}}}}}

    }
    return false;
  }

  private boolean containsVariable(TomTerm var, Instruction action) {
    try {
      tom_make_TopDown( new ContainsVariable(var) ).visitLight(action);
    } catch(VisitFailure e) {
      return true;
    }
    return false;
  }

  public static class ContainsVariable extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomterm.types.TomTerm  var;public ContainsVariable( tom.engine.adt.tomterm.types.TomTerm  var) {super(( new tom.library.sl.Identity() ));this.var=var;}public  tom.engine.adt.tomterm.types.TomTerm  getvar() {return var;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch141_5= false ; tom.engine.adt.tomname.types.TomName  tomMatch141_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch141_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch141_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch141_5= true ;tomMatch141_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch141_1= tomMatch141_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch141_5= true ;tomMatch141_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch141_1= tomMatch141_4.getAstName() ;}}}if (tomMatch141_5) {


        
        
        if(tomMatch141_1==var.getAstName()) { /* unamed block */
          throw new VisitFailure();
        }}}}}return _visit_BQTerm(tom__arg,introspector);}}public static class CollectFreeVar extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectFreeVar( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch142_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch142_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch142_2= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch142_4= true ;tomMatch142_2=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch142_4= true ;tomMatch142_3=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}}}if (tomMatch142_4) { tom.engine.adt.tomterm.types.TomTerm  tom___v=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);










        TomTerm newvar = tom___v.setOptions( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ); 
        if(!varList.contains(newvar)) { /* unamed block */ 
          varList.add(tom___v); 
        }
        throw new VisitFailure();
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
        
        throw new VisitFailure();
      }}}}return _visit_TomTerm(tom__arg,introspector);}}public static class CollectAliasVar extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectAliasVar( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch143_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getVar() ;boolean tomMatch143_7= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch143_6= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch143_5= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch143_1) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch143_7= true ;tomMatch143_5=tomMatch143_1;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch143_1) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch143_7= true ;tomMatch143_6=tomMatch143_1;}}}if (tomMatch143_7) { tom.engine.adt.tomterm.types.TomTerm  tom___v=tomMatch143_1;









        TomTerm newvar = tom___v.setOptions( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ); 
        if(!varList.contains(newvar)) { /* unamed block */ 
          varList.add(tom___v); 
        }
        throw new VisitFailure();
      }}}}}return _visit_Constraint(tom__arg,introspector);}}public static class CollectVariables extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectVariables( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch144_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch144_4= null ; tom.engine.adt.tomname.types.TomName  tomMatch144_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch144_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch144_5= true ;tomMatch144_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch144_1= tomMatch144_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch144_5= true ;tomMatch144_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch144_1= tomMatch144_4.getAstName() ;}}}if (tomMatch144_5) { tom.engine.adt.tomname.types.TomName  tom___name=tomMatch144_1;
















        
        if(!varList.contains(tom___name)) { /* unamed block */
          varList.add(tom___name); 
        }
        throw new VisitFailure();
      }}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch145_5= false ; tom.engine.adt.tomname.types.TomName  tomMatch145_1= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch145_4= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch145_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch145_5= true ;tomMatch145_3=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);tomMatch145_1= tomMatch145_3.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch145_5= true ;tomMatch145_4=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);tomMatch145_1= tomMatch145_4.getAstName() ;}}}if (tomMatch145_5) { tom.engine.adt.tomname.types.TomName  tom___name=tomMatch145_1;                 if(!varList.contains(tom___name)) { /* unamed block */           varList.add(tom___name);          }         throw new VisitFailure();       }}}}return _visit_TomTerm(tom__arg,introspector);}}



  
  private TomType getSubjectType(BQTerm subject, Collection<Constraint> constraints) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch146_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch146_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
        
        try { /* unamed block */
          Integer.parseInt( tomMatch146_1.getString() );
          return getSymbolTable().getIntType();
        } catch(java.lang.NumberFormatException e) { /* unamed block */
          return getSymbolTable().getStringType();
        }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch146_8= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch146_9= (( tom.engine.adt.code.types.BQTerm )subject).getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch146_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch146_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___type= tomMatch146_9.getTomType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch146_9.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) { tom.engine.adt.tomtype.types.TomType  tom___tomType=tomMatch146_9;


 
        if(tom___tomType==SymbolTable.TYPE_UNKNOWN) { /* unamed block */
          
          return guessSubjectType(subject,constraints);
        } else if(testTypeExistence(tom___type)) { /* unamed block */
          return tom___tomType;
        } else { /* unamed block */
          TomMessage.error(getLogger(),
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.unknownMatchArgumentTypeInSignature,
               tomMatch146_8.getString() , tom___type);                
        }}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch146_23= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch146_23) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___name= tomMatch146_23.getString() ;



        TomSymbol symbol = getSymbolFromName(tom___name);
        if(symbol!=null) { /* unamed block */
          TomType type = TomBase.getSymbolCodomain(symbol);
          String typeName = TomBase.getTomType(type);
          if(!testTypeExistence(typeName)) { /* unamed block */
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                tom___name, typeName);
          }          
          
          return type;
        } else { /* unamed block */
          
          return guessSubjectType(subject,constraints);
        }}}}}}


    return null;
  }

  private TomType getPatternType(TomTerm pattern) {
    { /* unamed block */{ /* unamed block */if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 pattern =  (( tom.engine.adt.tomterm.types.TomTerm )pattern).getTomTerm() ; }}}}{ /* unamed block */{ /* unamed block */if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch148_11= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch148_3= null ; tom.engine.adt.tomname.types.TomNameList  tomMatch148_1= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch148_4= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{ /* unamed block */tomMatch148_11= true ;tomMatch148_3=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch148_1= tomMatch148_3.getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{ /* unamed block */tomMatch148_11= true ;tomMatch148_4=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch148_1= tomMatch148_4.getNameList() ;}}}if (tomMatch148_11) {if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch148_1) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch148_1) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch148_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch148_9= tomMatch148_1.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch148_9) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___name= tomMatch148_9.getString() ;


        
        TomSymbol symbol = getSymbolFromName(tom___name);
        if(symbol!=null) { /* unamed block */
          TomType type = TomBase.getSymbolCodomain(symbol);
          
          String typeName = TomBase.getTomType(type);
          if(!testTypeExistence(typeName)) { /* unamed block */
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                tom___name, typeName);
          }
          return type;
        }}}}}}}}






















    return null;
  }

  
  private TomType guessSubjectType(BQTerm subject,Collection<Constraint> constraints) {
    for(Constraint constr:constraints) {
      { /* unamed block */{ /* unamed block */if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.code.types.BQTerm  tom___s= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ;

          
          
matchL:  { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {if ( (tom___s instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom___s) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() == (( tom.engine.adt.code.types.BQTerm )tom___s).getAstName() ) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstType() == (( tom.engine.adt.code.types.BQTerm )tom___s).getAstType() ) ) {
break matchL;}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {if ( (tom___s instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom___s) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() == (( tom.engine.adt.code.types.BQTerm )tom___s).getAstName() ) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getArgs() == (( tom.engine.adt.code.types.BQTerm )tom___s).getArgs() ) ) {
break matchL;}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (tom___s instanceof tom.engine.adt.code.types.BQTerm) ) {
 continue; }}}}


         TomType type = getPatternType( (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() );
         if(type!=null) { /* unamed block */
           return type;
         }}}}}


    }
    return null;
  }

  
  
  
  
  
  
  

  
  public static class CollectMatchConstraints extends tom.library.sl.AbstractStrategyBasic {private  java.util.List  constrList;public CollectMatchConstraints( java.util.List  constrList) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;}public  java.util.List  getconstrList() {return constrList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {

        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));         
        throw new VisitFailure();
      }}}}return _visit_Constraint(tom__arg,introspector);}}public static class CollectConstraints extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  constrList;private  java.util.Collection  lastAction;private  java.util.Map  orActionMap;public CollectConstraints( java.util.Collection  constrList,  java.util.Collection  lastAction,  java.util.Map  orActionMap) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;this.lastAction=lastAction;this.orActionMap=orActionMap;}public  java.util.Collection  getconstrList() {return constrList;}public  java.util.Collection  getlastAction() {return lastAction;}public  java.util.Map  getorActionMap() {return orActionMap;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {return ((T)visit_ConstraintInstruction((( tom.engine.adt.tominstruction.types.ConstraintInstruction )v),introspector));}if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  _visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.ConstraintInstruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {boolean tomMatch152_4= false ; tom.engine.adt.tomconstraint.types.Constraint  tomMatch152_3= null ; tom.engine.adt.tomconstraint.types.Constraint  tomMatch152_2= null ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {{ /* unamed block */tomMatch152_4= true ;tomMatch152_2=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);}} else {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {{ /* unamed block */tomMatch152_4= true ;tomMatch152_3=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);}}}if (tomMatch152_4) {















        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));         
        throw new VisitFailure();
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tom___oc=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);

        constrList.add(tom___oc);
        Iterator it = lastAction.iterator();
        orActionMap.put(tom___oc,it.next());
      }}}}return _visit_Constraint(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {         lastAction.clear();         lastAction.add( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getAction() );       }}}}return _visit_ConstraintInstruction(tom__arg,introspector);}}



  
  private void verifyMatchPattern(TomTerm term, TomType type) {      
    
    
    

    { /* unamed block */{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName ) (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        String fileName = findOriginTrackingFileName(tom___options);
        int decLine = findOriginTrackingLine(tom___options);
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.incorrectVariableStarInMatch, "_*");
        return;
      }}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch154_9= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch154_9) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        String fileName = findOriginTrackingFileName(tom___options);
        int decLine = findOriginTrackingLine(tom___options);
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.incorrectVariableStarInMatch,  tomMatch154_9.getString() );
        return;
      }}}}}

    
    
      
    
      validateTerm(term, type, false, true);
    
  }

  
  private void verifyStrategy(TomVisitList visitList) throws VisitFailure {
    { /* unamed block */{ /* unamed block */if ( (visitList instanceof tom.engine.adt.tomsignature.types.TomVisitList) ) {if ( (((( tom.engine.adt.tomsignature.types.TomVisitList )visitList) instanceof tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit) || ((( tom.engine.adt.tomsignature.types.TomVisitList )visitList) instanceof tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit)) ) { tom.engine.adt.tomsignature.types.TomVisitList  tomMatch155_end_4=(( tom.engine.adt.tomsignature.types.TomVisitList )visitList);do {{ /* unamed block */if (!( tomMatch155_end_4.isEmptyconcTomVisit() )) { tom.engine.adt.tomsignature.types.TomVisit  tomMatch155_10= tomMatch155_end_4.getHeadconcTomVisit() ;if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tomMatch155_10) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom___constraintInstructionList= tomMatch155_10.getAstConstraintInstructionList() ;

        List<MatchConstraint> matchConstraints = new ArrayList<MatchConstraint>();
        { /* unamed block */{ /* unamed block */if ( (tom___constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tomMatch156_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList);do {{ /* unamed block */if (!( tomMatch156_end_4.isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch156_8= tomMatch156_end_4.getHeadconcConstraintInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tomMatch156_8) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {

            matchConstraints.clear();
            tom_make_TopDownCollect( new CollectMatchConstraints(matchConstraints) ).visitLight( tomMatch156_8.getConstraint() );   
            
            
            
            MatchConstraint firstMatchConstr = matchConstraints.iterator().next(); 
            verifyMatchPattern(firstMatchConstr.getPattern(),  tomMatch155_10.getVNode() );
          }}if ( tomMatch156_end_4.isEmptyconcConstraintInstruction() ) {tomMatch156_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList);} else {tomMatch156_end_4= tomMatch156_end_4.getTailconcConstraintInstruction() ;}}} while(!( (tomMatch156_end_4==(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom___constraintInstructionList)) ));}}}}verifyMatch(tom___constraintInstructionList, tomMatch155_10.getOptions() )


;
      }}if ( tomMatch155_end_4.isEmptyconcTomVisit() ) {tomMatch155_end_4=(( tom.engine.adt.tomsignature.types.TomVisitList )visitList);} else {tomMatch155_end_4= tomMatch155_end_4.getTailconcTomVisit() ;}}} while(!( (tomMatch155_end_4==(( tom.engine.adt.tomsignature.types.TomVisitList )visitList)) ));}}}}

  }

  

    
    
    


  
  public TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel) {
    String termName = "emptyName";
    TomType type = null;
    int termClass = -1;
    String fileName = "unknown";
    int decLine = -1;
    Option orgTrack;

    



matchblock:{
    { /* unamed block */{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ; tom.engine.adt.tomname.types.TomNameList  tom___symbolNameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomterm.types.TomList  tom___arguments= (( tom.engine.adt.tomterm.types.TomTerm )term).getArgs() ;

        fileName = findOriginTrackingFileName(tom___options);
        decLine = findOriginTrackingLine(tom___options);
        termClass = TERM_APPL;

        TomSymbol symbol = ensureValidApplDisjunction(tom___symbolNameList, expectedType, fileName, decLine,  topLevel);

        if(symbol == null) { /* unamed block */
          
          break matchblock;
        }
        
        type = expectedType;
        TomName headName = tom___symbolNameList.getHeadconcTomName();
        if(headName instanceof AntiName) { /* unamed block */
          headName = ((AntiName)headName).getName();
        }
        termName = headName.getString();
        boolean listOp = (TomBase.isListOperator(symbol) || TomBase.isArrayOperator(symbol));
        if(listOp) { /* unamed block */
          
          
          
          validateListOperatorArgs(tom___arguments, symbol.getTypesToType().getDomain().getHeadconcTomType(),
              symbol.getTypesToType().getCodomain());
        } else { /* unamed block */
          
          TomTypeList types = symbol.getTypesToType().getDomain();
          int nbArgs = tom___arguments.length();
          int nbExpectedArgs = types.length();
          if(nbArgs != nbExpectedArgs) { /* unamed block */
            TomMessage.error(getLogger(),
                fileName, decLine, TomMessage.symbolNumberArgument,
                termName, Integer.valueOf(nbExpectedArgs), Integer.valueOf(nbArgs));
            break matchblock;
          }
          TomList args = tom___arguments;
          while(!args.isEmptyconcTomTerm()) { /* unamed block */
            
            
            validateTerm(args.getHeadconcTomTerm(), types.getHeadconcTomType(), listOp           , false);
            args = args.getTailconcTomTerm();
            types = types.getTailconcTomType();
          }}

        break matchblock;
      }}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ; tom.engine.adt.tomname.types.TomNameList  tom___symbolNameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomslot.types.SlotList  tom___slotList= (( tom.engine.adt.tomterm.types.TomTerm )term).getSlots() ;


        fileName = findOriginTrackingFileName(tom___options);
        decLine = findOriginTrackingLine(tom___options);
        termClass = RECORD_APPL;
        TomSymbol symbol = ensureValidRecordDisjunction(tom___symbolNameList, tom___slotList, expectedType, fileName, decLine, true);
        if(symbol == null) { /* unamed block */
          break matchblock;
        }{ /* unamed block */{ /* unamed block */if ( (tom___symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )tom___symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tom___symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158_end_4=(( tom.engine.adt.tomname.types.TomNameList )tom___symbolNameList);do {{ /* unamed block */if (!( tomMatch158_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158_8= tomMatch158_end_4.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch158_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {







            verifyRecordStructure(tom___options,  tomMatch158_8.getString() , tom___slotList, fileName,decLine);
          }}if ( tomMatch158_end_4.isEmptyconcTomName() ) {tomMatch158_end_4=(( tom.engine.adt.tomname.types.TomNameList )tom___symbolNameList);} else {tomMatch158_end_4= tomMatch158_end_4.getTailconcTomName() ;}}} while(!( (tomMatch158_end_4==(( tom.engine.adt.tomname.types.TomNameList )tom___symbolNameList)) ));}}}}


        type = expectedType;
        TomName headName = tom___symbolNameList.getHeadconcTomName();
        if(headName instanceof AntiName) { /* unamed block */
          headName = ((AntiName)headName).getName();
        }
        termName = headName.getString();
        break matchblock;
      }}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch157_14= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch157_14) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        termClass = VARIABLE;
        fileName = findOriginTrackingFileName(tom___options);
        decLine = findOriginTrackingLine(tom___options);
        type = null;
        termName =  tomMatch157_14.getString() ;
        break matchblock;
      }}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch157_22= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch157_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        termClass = VARIABLE_STAR;
        fileName = findOriginTrackingFileName(tom___options);
        decLine = findOriginTrackingLine(tom___options);
        type = null;
        termName =  tomMatch157_22.getString() +"*";
        if(!listSymbol) { /* unamed block */
          TomMessage.error(getLogger(),
              fileName,
              decLine,
              TomMessage.invalidVariableStarArgument,
              termName);
        }
        break matchblock;
      }}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName ) (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        termClass = UNAMED_VARIABLE;
        fileName = findOriginTrackingFileName(tom___options);
        decLine = findOriginTrackingLine(tom___options);
        type = null;
        termName = "unamed";
        break matchblock;
      }}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName ) (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {


        termClass = UNAMED_VARIABLE_STAR;
        fileName = findOriginTrackingFileName(tom___options);
        decLine = findOriginTrackingLine(tom___options);
        type = null;
        termName = "unamed*";
        if(!listSymbol) { /* unamed block */
          TomMessage.error(getLogger(),
              fileName,
              decLine,
              TomMessage.invalidVariableStarArgument,
              termName);
        }
        break matchblock;
      }}}}}


    throw new TomRuntimeException("Strange Term "+term);
    } 
    return new TermDescription(termClass, termName, fileName,decLine, type);
  }

  private TomSymbol ensureValidApplDisjunction(TomNameList symbolNameList, TomType expectedType, 
      String fileName, int decLine,  boolean topLevel) {

    

    if(symbolNameList.length()==1) { 
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);

      

      if(symbol == null ) {
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbol, res);
        return null;
      } else { 
        if (!getOptionBooleanValue("newtyper")) {
          if( !topLevel ) {
            if(!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
              return null;
            }
          }
        }
      }
      return symbol;
    } else {
      
      TomSymbol symbol = null;
      TomTypeList domainReference = null;
      PairNameDeclList slotReference = null;
      String nameReference = null;
      { /* unamed block */{ /* unamed block */if ( (symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch159_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);do {{ /* unamed block */if (!( tomMatch159_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch159_8= tomMatch159_end_4.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch159_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___dijName= tomMatch159_8.getString() ;
 
          symbol = getSymbolFromName(tom___dijName);
          if(symbol == null) { /* unamed block */
            TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbolInDisjunction,tom___dijName);
            return null;
          }
          if( !topLevel ) { /* unamed block */
            
            if(!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, tom___dijName, fileName,decLine)) { /* unamed block */
              return null;
            }}


          if(domainReference == null) { /* unamed block */ 
            domainReference = TomBase.getSymbolDomain(symbol);
            slotReference = symbol.getPairNameDeclList();
            nameReference = tom___dijName;
          } else { /* unamed block */
            if(TomBase.getSymbolDomain(symbol) != domainReference) { /* unamed block */
              TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, nameReference, tom___dijName);
              return null;
            }
            if(symbol.getPairNameDeclList() != slotReference) { /* unamed block */
              PairNameDeclList l1 = slotReference;
              PairNameDeclList l2 = symbol.getPairNameDeclList();
              while(!l1.isEmptyconcPairNameDecl()) { /* unamed block */
                if(l1.getHeadconcPairNameDecl().getSlotName() != l2.getHeadconcPairNameDecl().getSlotName()) { /* unamed block */
                  TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, nameReference, tom___dijName);
                  return null;
                }
                l1=l1.getTailconcPairNameDecl();
                l2=l2.getTailconcPairNameDecl();
              }}}}}if ( tomMatch159_end_4.isEmptyconcTomName() ) {tomMatch159_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);} else {tomMatch159_end_4= tomMatch159_end_4.getTailconcTomName() ;}}} while(!( (tomMatch159_end_4==(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) ));}}}}




      return symbol;
    }
  } 

  private boolean ensureSymbolCodomain(TomType currentCodomain, TomType expectedType, TomMessage msg, String symbolName, String fileName,int decLine) {
    { /* unamed block */{ /* unamed block */if ( (currentCodomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )currentCodomain) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( (expectedType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )expectedType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  (( tom.engine.adt.tomtype.types.TomType )currentCodomain).getTomType() .equals( (( tom.engine.adt.tomtype.types.TomType )expectedType).getTomType() ) ) {

        return true;
      }}}}}}}

    
    TomMessage.error(getLogger(),fileName,decLine, msg,
        symbolName, currentCodomain.getTomType(), expectedType.getTomType());
    return false;
  } 

  private TomSymbol ensureValidRecordDisjunction(TomNameList symbolNameList, SlotList slotList, 
      TomType expectedType, String fileName, int decLine, boolean topLevel) {
    if(symbolNameList.length()==1) { 
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);
      if (symbol == null ) { 
        
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbol, res);
        return null;
      } else { 
        
        if (!getOptionBooleanValue("newtyper")) {
          if ( !topLevel ) {
            if (!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
              return null;
            }
          }
        }
      }
      return symbol;
    } else {
      TomSymbol symbol = null;
      TomSymbol referenceSymbol = null;
      TomTypeList referenceDomain = null;
      String referenceName = null;
      { /* unamed block */{ /* unamed block */if ( (symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch161_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);do {{ /* unamed block */if (!( tomMatch161_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch161_8= tomMatch161_end_4.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch161_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___dijName= tomMatch161_8.getString() ;
 
          symbol =  getSymbolFromName(tom___dijName);
          if(symbol == null) { /* unamed block */
            
            TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbolInDisjunction, tom___dijName);
            return null;
          }
          if( !topLevel ) { /* unamed block */
            
            if (!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, tom___dijName, fileName,decLine)) { /* unamed block */
              return null;
            }}

          

          if(referenceDomain == null) { /* unamed block */ 
            referenceSymbol = symbol;
            referenceName = tom___dijName;
            referenceDomain = TomBase.getSymbolDomain(symbol);
          } else { /* unamed block */
            
            TomTypeList currentDomain = TomBase.getSymbolDomain(symbol);
            
            while(!slotList.isEmptyconcSlot()) { /* unamed block */
              Slot slot = slotList.getHeadconcSlot();
              TomName slotName = slot.getSlotName();
              int currentSlotIndex = TomBase.getSlotIndex(symbol,slotName);
              int referenceSlotIndex = TomBase.getSlotIndex(referenceSymbol,slotName);

              
              
              
              
              
              

              if (referenceSlotIndex == -1){ /* unamed block */
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionSlotName, referenceName,((Name)slotName).getString());
                return null;                
              }

              if (currentSlotIndex == -1){ /* unamed block */
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionSlotName, tom___dijName,((Name)slotName).getString());
                return null;                
              }

              if(TomBase.elementAt(currentDomain,currentSlotIndex) != TomBase.elementAt(referenceDomain,referenceSlotIndex)) { /* unamed block */
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, referenceName, tom___dijName);
                return null;
              }

              slotList = slotList.getTailconcSlot();
            }}}}if ( tomMatch161_end_4.isEmptyconcTomName() ) {tomMatch161_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);} else {tomMatch161_end_4= tomMatch161_end_4.getTailconcTomName() ;}}} while(!( (tomMatch161_end_4==(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) ));}}}}




      return symbol;
    }
  } 

  
  
  
  private void verifyRecordStructure(OptionList option, String tomName, SlotList slotList, String fileName, int decLine)  {
    TomSymbol symbol = getSymbolFromName(tomName);
    if(symbol != null) {
      
      
      

      
      
      
      
      
      verifyRecordSlots(slotList,symbol, TomBase.getSymbolDomain(symbol), tomName, fileName, decLine);
    } else {
      TomMessage.error(getLogger(),fileName,decLine,
          TomMessage.unknownSymbol,
          tomName);
    }
  } 

  
  
  private void verifyRecordSlots(SlotList slotList, TomSymbol tomSymbol, TomTypeList typeList, String methodName, String fileName, int decLine) {
    
    Collection<String> listOfPossibleSlot = null;
    Collection<Integer> studiedSlotIndexList = new HashSet<Integer>();
    
    while( !slotList.isEmptyconcSlot() ) {
      TomName pairSlotName = slotList.getHeadconcSlot().getSlotName();
      
      int index = TomBase.getSlotIndex(tomSymbol,pairSlotName);
      if(index < 0) {
        if(listOfPossibleSlot == null) {
          
          listOfPossibleSlot = new HashSet<String>();
          PairNameDeclList listOfSlots = tomSymbol.getPairNameDeclList();
          while ( !listOfSlots.isEmptyconcPairNameDecl() ) {
            TomName sname = listOfSlots.getHeadconcPairNameDecl().getSlotName();
            if(!sname.isEmptyName()) {
              listOfPossibleSlot.add(sname.getString());
            }
            listOfSlots = listOfSlots.getTailconcPairNameDecl();
          }
        }
        TomMessage.error(getLogger(),fileName,decLine,
            TomMessage.badSlotName,
            pairSlotName.getString(), methodName, listOfPossibleSlot.toString());
        return; 
      } else { 
        
        if(pairSlotName.isName() && studiedSlotIndexList.contains(index)) {
          
          
          TomMessage.error(getLogger(),fileName,decLine,
              TomMessage.slotRepeated,
              methodName, pairSlotName.getString());
          return; 
        }
        studiedSlotIndexList.add(index);
      }

      
      PairNameDeclList listOfSlots =  tomSymbol.getPairNameDeclList();
      TomTypeList listOfTypes = typeList;
      while(!listOfSlots.isEmptyconcPairNameDecl()) {
        SlotList listOfPair = slotList;
        TomName slotName = listOfSlots.getHeadconcPairNameDecl().getSlotName();
        TomType expectedType = listOfTypes.getHeadconcTomType();
        if(!slotName.isEmptyName()) {
          

whileBlock: {
              while(!listOfPair.isEmptyconcSlot()) {
                Slot pairSlotTerm = listOfPair.getHeadconcSlot();
                { /* unamed block */{ /* unamed block */if ( (slotName instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )slotName) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (pairSlotTerm instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )pairSlotTerm) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch162_5= (( tom.engine.adt.tomslot.types.Slot )pairSlotTerm).getSlotName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch162_5) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  (( tom.engine.adt.tomname.types.TomName )slotName).getString() .equals( tomMatch162_5.getString() ) ) {

                    validateTerm( (( tom.engine.adt.tomslot.types.Slot )pairSlotTerm).getAppl()  ,expectedType, false, true);
                    break whileBlock;
                  }}}}}}}{ /* unamed block */if ( (slotName instanceof tom.engine.adt.tomname.types.TomName) ) {if ( (pairSlotTerm instanceof tom.engine.adt.tomslot.types.Slot) ) {
listOfPair = listOfPair.getTailconcSlot();}}}}

              }
            }
        }
        
        listOfSlots = listOfSlots.getTailconcPairNameDecl();
        listOfTypes = listOfTypes.getTailconcTomType();
      }

      slotList = slotList.getTailconcSlot();
    }
  } 

  public void validateListOperatorArgs(TomList args, TomType expectedType, TomType parentListCodomain) {
    { /* unamed block */{ /* unamed block */if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )args) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch163_end_4=(( tom.engine.adt.tomterm.types.TomList )args);do {{ /* unamed block */if (!( tomMatch163_end_4.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch163_7= tomMatch163_end_4.getHeadconcTomTerm() ;boolean tomMatch163_10= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch163_8= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch163_9= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch163_7) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{ /* unamed block */tomMatch163_10= true ;tomMatch163_8=tomMatch163_7;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch163_7) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{ /* unamed block */tomMatch163_10= true ;tomMatch163_9=tomMatch163_7;}}}if (tomMatch163_10) { tom.engine.adt.tomterm.types.TomTerm  tom___arg= tomMatch163_end_4.getHeadconcTomTerm() ;

        TomSymbol argSymbol = getSymbolFromName(getName(tom___arg));
        
        if(TomBase.isListOperator(argSymbol)) { /* unamed block */
          
          
          
          if(argSymbol.getTypesToType().getCodomain() == parentListCodomain) { /* unamed block */
            validateTerm(tom___arg, parentListCodomain, true, false);            
          } else { /* unamed block */
            validateTerm(tom___arg, expectedType, true, false);    
          }}
 else { /* unamed block */
          validateTerm(tom___arg, expectedType, true, false);
        }}}if ( tomMatch163_end_4.isEmptyconcTomTerm() ) {tomMatch163_end_4=(( tom.engine.adt.tomterm.types.TomList )args);} else {tomMatch163_end_4= tomMatch163_end_4.getTailconcTomTerm() ;}}} while(!( (tomMatch163_end_4==(( tom.engine.adt.tomterm.types.TomList )args)) ));}}}}


  } 

  private boolean testTypeExistence(String typeName) {
    return getSymbolTable().getType(typeName) != null;
  }

  protected class TermDescription {
    private int termClass;
    private String fileName;
    private int decLine;
    private String name ="";
    private TomType tomType = null;

    public TermDescription(int termClass, String name, String fileName, int decLine, TomType tomType) {
      this.termClass = termClass;
      this.fileName = fileName;
      this.decLine = decLine;
      this.name = name;
      this.tomType = tomType;
    }

    public int getTermClass() {
      return termClass;
    }

    public String getName() {
      return name;
    }

    public String getFileName() {
      return fileName;
    }

    public int getLine() {
      return decLine;
    }

    public TomType getType() {
      if(tomType != null && !tomType.isEmptyType()) {
        return tomType;
      } else {
        return  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
      }
    }
  } 

  
  private void verifyBQAppl(BQTerm subject) throws VisitFailure {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch164_2= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom___options= (( tom.engine.adt.code.types.BQTerm )subject).getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch164_2) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___name= tomMatch164_2.getString() ;

        int decLine = findOriginTrackingLine(tom___options);
        String fileName = findOriginTrackingFileName(tom___options);
        TomSymbol symbol = getSymbolFromName(tom___name);
        if(symbol==null) { /* unamed block */}


 else { /* unamed block */
          
          
          if(!TomBase.isArrayOperator(symbol) && !TomBase.isListOperator(symbol)) { /* unamed block */
            TomTypeList types = symbol.getTypesToType().getDomain();
            int nbExpectedArgs = types.length();
            int nbArgs =  (( tom.engine.adt.code.types.BQTerm )subject).getArgs() .length();
            if(nbArgs != nbExpectedArgs) { /* unamed block */
              TomMessage.error(getLogger(), fileName, decLine,
                  TomMessage.symbolNumberArgument,
                  tom___name, nbExpectedArgs, nbArgs);
            }}}}}}}}




  }

  private String getName(TomTerm term) {
    { /* unamed block */{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch165_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch165_1) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch165_1) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch165_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch165_7= tomMatch165_1.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch165_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch165_1.getTailconcTomName() .isEmptyconcTomName() ) {
 return  tomMatch165_7.getString() ;}}}}}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom___nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String dijunctionName = tom___nameList.getHeadconcTomName().getString();
        while(!tom___nameList.isEmptyconcTomName()) { /* unamed block */
          String head = tom___nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom___nameList = tom___nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch165_14= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch165_14) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch165_14) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch165_14.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch165_20= tomMatch165_14.getHeadconcTomName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch165_20) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch165_14.getTailconcTomName() .isEmptyconcTomName() ) {
 return  tomMatch165_20.getString() ;}}}}}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom___nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String dijunctionName = tom___nameList.getHeadconcTomName().getString();
        while(!tom___nameList.isEmptyconcTomName()) { /* unamed block */
          String head = tom___nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom___nameList = tom___nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch165_27= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch165_27) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch165_27.getString() ;}}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch165_34= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch165_34) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch165_34.getString() +"*";}}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getName( (( tom.engine.adt.tomterm.types.TomTerm )term).getTomTerm() ); }}}}

    throw new TomRuntimeException("Invalid Term:" + term);
  }

  private String getName(BQTerm term) {
    { /* unamed block */{ /* unamed block */if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch166_1= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch166_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch166_1.getString() ;}}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch166_8= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch166_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch166_8.getString() ;}}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch166_15= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch166_15) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch166_15.getString() +"*";}}}}{ /* unamed block */if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch166_22= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch166_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch166_22.getString() ;}}}}}

    throw new TomRuntimeException("Invalid Term:" + term);
  }
} 
