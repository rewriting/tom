
/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Inria
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
 **/

package tom.engine.checker;

import java.util.*;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.exception.TomRuntimeException;

import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;
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

/**
 * The syntax checker plugin.
 */
public class SyntaxCheckerPlugin extends TomGenericPlugin {

        private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_append_list_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList l1,  tom.engine.adt.tomsignature.types.TomVisitList  l2) {     if( l1.isEmptyconcTomVisit() ) {       return l2;     } else if( l2.isEmptyconcTomVisit() ) {       return l1;     } else if(  l1.getTailconcTomVisit() .isEmptyconcTomVisit() ) {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( l1.getHeadconcTomVisit() ,tom_append_list_concTomVisit( l1.getTailconcTomVisit() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomVisitList  tom_get_slice_concTomVisit( tom.engine.adt.tomsignature.types.TomVisitList  begin,  tom.engine.adt.tomsignature.types.TomVisitList  end, tom.engine.adt.tomsignature.types.TomVisitList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomVisit()  ||  (end== tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit.make( begin.getHeadconcTomVisit() ,( tom.engine.adt.tomsignature.types.TomVisitList )tom_get_slice_concTomVisit( begin.getTailconcTomVisit() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_append_list_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList l1,  tom.engine.adt.tomdeclaration.types.DeclarationList  l2) {     if( l1.isEmptyconcDeclaration() ) {       return l2;     } else if( l2.isEmptyconcDeclaration() ) {       return l1;     } else if(  l1.getTailconcDeclaration() .isEmptyconcDeclaration() ) {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,l2) ;     } else {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,tom_append_list_concDeclaration( l1.getTailconcDeclaration() ,l2)) ;     }   }   private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_get_slice_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList  begin,  tom.engine.adt.tomdeclaration.types.DeclarationList  end, tom.engine.adt.tomdeclaration.types.DeclarationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcDeclaration()  ||  (end== tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( begin.getHeadconcDeclaration() ,( tom.engine.adt.tomdeclaration.types.DeclarationList )tom_get_slice_concDeclaration( begin.getTailconcDeclaration() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {     if( l1.isEmptyconcPairNameDecl() ) {       return l2;     } else if( l2.isEmptyconcPairNameDecl() ) {       return l1;     } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}      







  // Different kind of structures
  protected final static int TERM_APPL               = 0;
  protected final static int APPL_DISJUNCTION        = 2;
  protected final static int RECORD_APPL             = 3;
  protected final static int RECORD_APPL_DISJUNCTION = 4;
  protected final static int XML_APPL                = 5;
  protected final static int VARIABLE_STAR           = 6;
  protected final static int VARIABLE                = 7;
  protected final static int UNAMED_VARIABLE         = 8;
  protected final static int UNAMED_VARIABLE_STAR    = 9;

  private Option currentTomStructureOrgTrack;

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='noSyntaxCheck' altName='' description='Do not perform syntax checking' value='false'/>" +
    "</options>";

  /** op and type declarator */
  private final static String OPERATOR    = "Operator";
  private final static String CONSTRUCTOR = "%op";
  private final static String OP_ARRAY    = "%oparray";
  private final static String OP_LIST     = "%oplist";
  private final static String TYPE        = "Type";
  private final static String TYPE_TERM   = "%typeterm";

  /** type function symbols */
  private final static String EQUALS      = "equals";
  private final static String GET_ELEMENT = "get_element";
  private final static String GET_SIZE    = "get_size";
  private final static String GET_HEAD    = "get_head";
  private final static String GET_TAIL    = "get_tail";
  private final static String IS_EMPTY    = "is_empty";
  /** operator function symbols */
  private final static String MAKE_APPEND = "make_append";
  private final static String MAKE_EMPTY  = "make_empty";
  private final static String MAKE_INSERT = "make_insert";
  private final static String MAKE        = "make";

  /** the list of already studied and declared Types */
  private  Collection<String> alreadyStudiedTypes =  null;
  /** the list of already studied and declared Symbol */
  private  Collection<String> alreadyStudiedSymbols =  null;

  /** List of expected functional declaration in each type declaration */
  private final static List<String> TypeTermSignature =
    new ArrayList<String>(Arrays.asList(new String[]{ SyntaxCheckerPlugin.EQUALS }));

  /** Constructor */
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

  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(SyntaxCheckerPlugin.DECLARED_OPTIONS);
  }

  protected void reinit() {
    currentTomStructureOrgTrack = null;
    alreadyStudiedTypes   = new HashSet<String>();
    alreadyStudiedSymbols = new HashSet<String>();
  }
  
  /**
   * Shared Functions 
   */

  protected String findOriginTrackingFileName(OptionList optionList) {
    {{if ( (optionList instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )(( tom.engine.adt.tomoption.types.OptionList )optionList)) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )(( tom.engine.adt.tomoption.types.OptionList )optionList)) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch121_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);do {{if (!( tomMatch121_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch121_8= tomMatch121_end_4.getHeadconcOption() ;if ( (tomMatch121_8 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch121_8) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return  tomMatch121_8.getFileName() ; }}}if ( tomMatch121_end_4.isEmptyconcOption() ) {tomMatch121_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);} else {tomMatch121_end_4= tomMatch121_end_4.getTailconcOption() ;}}} while(!( (tomMatch121_end_4==(( tom.engine.adt.tomoption.types.OptionList )optionList)) ));}}}}

    return "unknown filename";
  }

  protected int findOriginTrackingLine(OptionList optionList) {
    {{if ( (optionList instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )(( tom.engine.adt.tomoption.types.OptionList )optionList)) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )(( tom.engine.adt.tomoption.types.OptionList )optionList)) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch122_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);do {{if (!( tomMatch122_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch122_8= tomMatch122_end_4.getHeadconcOption() ;if ( (tomMatch122_8 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch122_8) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return  tomMatch122_8.getLine() ; }}}if ( tomMatch122_end_4.isEmptyconcOption() ) {tomMatch122_end_4=(( tom.engine.adt.tomoption.types.OptionList )optionList);} else {tomMatch122_end_4= tomMatch122_end_4.getTailconcOption() ;}}} while(!( (tomMatch122_end_4==(( tom.engine.adt.tomoption.types.OptionList )optionList)) ));}}}}

    return -1;
  }

  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom SyntaxChecker : TSM"+getStreamManager().toString());
    if(isActivated()) {
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        try {
          Code code = (Code)getWorkingTerm();
          //System.out.println("code = " + code);
          tom_make_TopDownCollect(tom_make_CheckSyntax(this)).visitLight(code);
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("strategy failed");
        }
        // verbose
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
      // syntax checker desactivated
      TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          TomMessage.syntaxCheckerInactivated);
    }
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noSyntaxCheck");
  }

  /**
   * Syntax checking entry point: Catch and verify all type and operator
   * declaration, Match instruction
   */
  public static class CheckSyntax extends tom.library.sl.AbstractStrategyBasic {private  SyntaxCheckerPlugin  scp;public CheckSyntax( SyntaxCheckerPlugin  scp) {super(( new tom.library.sl.Identity() ));this.scp=scp;}public  SyntaxCheckerPlugin  getscp() {return scp;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {







































































        scp.verifyBQAppl((( tom.engine.adt.code.types.BQTerm )tom__arg));
      }}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )tom__arg)) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {         /* TOM MATCH STRUCTURE */         scp.verifyMatch( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() ,  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() );       }}}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg)) instanceof tom.engine.adt.tomdeclaration.types.declaration.Strategy) ) { tom.engine.adt.tomsignature.types.TomVisitList  tom_list= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getVisitList() ; tom.engine.adt.tomoption.types.Option  tom_origin= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() ; tom.engine.adt.tomdeclaration.types.Declaration  tom_s=(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg);         if(tom_list.isEmptyconcTomVisit()) {           {{if ( (tom_origin instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tom_origin) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )tom_origin)) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {                TomMessage.error(scp.getLogger(),  (( tom.engine.adt.tomoption.types.Option )tom_origin).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )tom_origin).getLine() , TomMessage.emptyStrategy);               return tom_s;             }}}}}           TomMessage.error(scp.getLogger(), null, -1, TomMessage.emptyStrategy);           return tom_s;         }         /* STRATEGY MATCH STRUCTURE */         scp.verifyStrategy(tom_list);         throw new tom.library.sl.VisitFailure();/* stop the top-down */       }}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg)) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch125_6= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch125_6 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch125_6) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifyTypeDecl(SyntaxCheckerPlugin.TYPE_TERM,  tomMatch125_6.getString() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getDeclarations() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() );         throw new tom.library.sl.VisitFailure();/* stop the top-down */       }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg)) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch125_15= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch125_15 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch125_15) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifySymbol(SyntaxCheckerPlugin.CONSTRUCTOR, scp.getSymbolFromName( tomMatch125_15.getString() ));         throw new tom.library.sl.VisitFailure();/* stop the top-down */       }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch125_22= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch125_22 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch125_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifySymbol(SyntaxCheckerPlugin.OP_ARRAY, scp.getSymbolFromName( tomMatch125_22.getString() ));         throw new tom.library.sl.VisitFailure();/* stop the top-down */       }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch125_29= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch125_29 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch125_29) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         scp.verifySymbol(SyntaxCheckerPlugin.OP_LIST, scp.getSymbolFromName( tomMatch125_29.getString() ));         throw new tom.library.sl.VisitFailure();/* stop the top-down */       }}}}}}}return _visit_Declaration(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CheckSyntax( SyntaxCheckerPlugin  t0) { return new CheckSyntax(t0);}



  // /////////////////////////////
  // TYPE DECLARATION CONCERNS //
  // ////////////////////////////
  private void verifyTypeDecl(String declType, String tomName, DeclarationList listOfDeclaration, Option typeOrgTrack) {
    setCurrentTomStructureOrgTrack(typeOrgTrack);
    // ensure first definition
    verifyMultipleDefinition(tomName, declType, TYPE);
    // verify Macro functions
    List<String> verifyList = new ArrayList<String>(SyntaxCheckerPlugin.TypeTermSignature);

    {{if ( (listOfDeclaration instanceof tom.engine.adt.tomdeclaration.types.DeclarationList) ) {if ( (((( tom.engine.adt.tomdeclaration.types.DeclarationList )(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration)) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration) || ((( tom.engine.adt.tomdeclaration.types.DeclarationList )(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration)) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration)) ) { tom.engine.adt.tomdeclaration.types.DeclarationList  tomMatch127_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration);do {{if (!( tomMatch127_end_4.isEmptyconcDeclaration() )) { tom.engine.adt.tomdeclaration.types.Declaration  tom_d= tomMatch127_end_4.getHeadconcDeclaration() ;
 // for each Declaration
matchblock:{
             {{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch128_1= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getTermArg1() ; tom.engine.adt.code.types.BQTerm  tomMatch128_2= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getTermArg2() ;if ( (tomMatch128_1 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch128_1) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch128_7= tomMatch128_1.getAstName() ;if ( (tomMatch128_7 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch128_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch128_2 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch128_2) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch128_13= tomMatch128_2.getAstName() ;if ( (tomMatch128_13 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch128_13) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.EQUALS,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() , tomMatch128_7.getString() , tomMatch128_13.getString() ,declType)


;
                 break matchblock;
               }}}}}}}}}}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {checkField(SyntaxCheckerPlugin.GET_HEAD,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() ,declType)


;
                 break matchblock;
               }}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {checkField(SyntaxCheckerPlugin.GET_TAIL,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() ,declType)

;
                 break matchblock;
               }}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {checkField(SyntaxCheckerPlugin.IS_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() ,declType)

;
                 break matchblock;
               }}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch128_32= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getVariable() ; tom.engine.adt.code.types.BQTerm  tomMatch128_33= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getIndex() ;if ( (tomMatch128_32 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch128_32) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch128_37= tomMatch128_32.getAstName() ;if ( (tomMatch128_37 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch128_37) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch128_33 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch128_33) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch128_43= tomMatch128_33.getAstName() ;if ( (tomMatch128_43 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch128_43) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.GET_ELEMENT,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() , tomMatch128_37.getString() , tomMatch128_43.getString() ,declType)


;
                 break matchblock;
               }}}}}}}}}}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {checkField(SyntaxCheckerPlugin.GET_SIZE,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() ,declType)

;
                 break matchblock;
               }}}}}

           }
      }if ( tomMatch127_end_4.isEmptyconcDeclaration() ) {tomMatch127_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration);} else {tomMatch127_end_4= tomMatch127_end_4.getTailconcDeclaration() ;}}} while(!( (tomMatch127_end_4==(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration)) ));}}}}

    // remove non mandatory functions
    if(verifyList.contains(SyntaxCheckerPlugin.EQUALS)) {
      verifyList.remove(verifyList.indexOf(SyntaxCheckerPlugin.EQUALS));
    }
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(declType, verifyList);
    }
  } // verifyTypeDecl

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
  } // verifyMultipleDefinition

  private void checkField(String function, List foundFunctions, Option orgTrack, String symbolType) {
    if(foundFunctions.contains(function)) {
      foundFunctions.remove(foundFunctions.indexOf(function));
    } else {
      TomMessage.error(getLogger(), orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.macroFunctionRepeated,
          function);
    }
  } // checkField

  private  void checkFieldAndLinearArgs(String function, List foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      TomMessage.error(getLogger(), orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.nonLinearMacroFunction,
          function, name1);
    }
  } // checkFieldAndLinearArgs

  // ///////////////////////////////
  // SYMBOL DECLARATION CONCERNS //
  // ///////////////////////////////
  private void verifySymbol(String symbolType, TomSymbol tomSymbol){
    int domainLength;
    String symbStrName = tomSymbol.getAstName().getString();

    OptionList optionList = tomSymbol.getOptions();
    // We save first the origin tracking of the symbol declaration
    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(optionList));

    // ensure first definition then Codomain, Domain, Macros and Slots (Simple
    // operator)
    verifyMultipleDefinition(symbStrName, symbolType, SyntaxCheckerPlugin.OPERATOR);
    verifySymbolCodomain(TomBase.getSymbolCodomain(tomSymbol), symbStrName, symbolType);
    domainLength = verifySymbolDomain(TomBase.getSymbolDomain(tomSymbol), symbStrName, symbolType);
    verifySymbolMacroFunctions(optionList, domainLength, symbolType);
  } // verifySymbol

  private void verifySymbolCodomain(TomType codomain, String symbName, String symbolType) {
    {{if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )codomain)) instanceof tom.engine.adt.tomtype.types.tomtype.Codomain) ) {

        if(getSymbolTable().getSymbolFromName( (( tom.engine.adt.tomtype.types.TomType )codomain).getAstName() ) == null) {
          TomMessage.error(getLogger(), 
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              symbName, codomain);
        }
        return;
      }}}}{if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )codomain)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch129_7= (( tom.engine.adt.tomtype.types.TomType )codomain).getTlType() ; String  tom_typeName= (( tom.engine.adt.tomtype.types.TomType )codomain).getTomType() ;if ( (tomMatch129_7 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch129_7) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {


        if(!testTypeExistence(tom_typeName)) {
          TomMessage.error(getLogger(),
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              symbName, tom_typeName);
          if(tom_typeName.equals("Strategy")) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.missingIncludeSL);
          }
        }
        return;
      }}}}}}{if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )codomain)) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {


        TomMessage.error(getLogger(),
            getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.symbolCodomainError,
            symbName, "");
        return;
      }}}}}

    throw new TomRuntimeException("Strange codomain " + codomain);
  }

  private int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
    int position = 1;
    if(symbolType.equals(SyntaxCheckerPlugin.CONSTRUCTOR)) {
      {{if ( (args instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )args)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )args)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch130_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )args);do {{if (!( tomMatch130_end_4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch130_10= tomMatch130_end_4.getHeadconcTomType() ;if ( (tomMatch130_10 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch130_10) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch130_9= tomMatch130_10.getTlType() ; String  tom_typeName= tomMatch130_10.getTomType() ;if ( (tomMatch130_9 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch130_9) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {
 // for each symbol types
          if(!testTypeExistence(tom_typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.symbolDomainError,
                Integer.valueOf(position), symbName, tom_typeName);
          }
          position++;
        }}}}}if ( tomMatch130_end_4.isEmptyconcTomType() ) {tomMatch130_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )args);} else {tomMatch130_end_4= tomMatch130_end_4.getTailconcTomType() ;}}} while(!( (tomMatch130_end_4==(( tom.engine.adt.tomtype.types.TomTypeList )args)) ));}}}}

      return (position-1);
    } else { // OPARRAY and OPLIST
      {{if ( (args instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )args)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )args)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( (( tom.engine.adt.tomtype.types.TomTypeList )args).isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch131_6= (( tom.engine.adt.tomtype.types.TomTypeList )args).getHeadconcTomType() ;if ( (tomMatch131_6 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch131_6) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch131_5= tomMatch131_6.getTlType() ; String  tom_typeName= tomMatch131_6.getTomType() ;if ( (tomMatch131_5 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch131_5) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {if (  (( tom.engine.adt.tomtype.types.TomTypeList )args).getTailconcTomType() .isEmptyconcTomType() ) {

          if(!testTypeExistence(tom_typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.listSymbolDomainError,
                symbName, tom_typeName);
          }
        }}}}}}}}}}
 // match
      return 1;
    }
  } // verifySymbolDomain

  private void verifySymbolMacroFunctions(OptionList option, int domainLength, String symbolType) {
    List<String> verifyList = new ArrayList<String>();
    boolean foundOpMake = false;
    if(symbolType.equals(SyntaxCheckerPlugin.CONSTRUCTOR)) { // Nothing absolutely
      // necessary
    } else if(symbolType == SyntaxCheckerPlugin.OP_ARRAY ) {
      verifyList.add(SyntaxCheckerPlugin.MAKE_EMPTY);
      verifyList.add(SyntaxCheckerPlugin.MAKE_APPEND);
    } else if(symbolType == SyntaxCheckerPlugin.OP_LIST) {
      verifyList.add(SyntaxCheckerPlugin.MAKE_EMPTY);
      verifyList.add(SyntaxCheckerPlugin.MAKE_INSERT);
    }

    {{if ( (option instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )(( tom.engine.adt.tomoption.types.OptionList )option)) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )(( tom.engine.adt.tomoption.types.OptionList )option)) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch132_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);do {{if (!( tomMatch132_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch132_8= tomMatch132_end_4.getHeadconcOption() ;if ( (tomMatch132_8 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch132_8) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) { tom.engine.adt.tomdeclaration.types.Declaration  tom_d= tomMatch132_8.getAstDeclaration() ;
 // for each Declaration
matchblock:{
             {{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {checkField(SyntaxCheckerPlugin.MAKE_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() ,symbolType)


;
                 break matchblock;
               }}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.code.types.BQTerm  tomMatch133_5= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getVarList() ; tom.engine.adt.code.types.BQTerm  tomMatch133_6= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getVarElt() ;if ( (tomMatch133_5 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch133_5) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch133_10= tomMatch133_5.getAstName() ;if ( (tomMatch133_10 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch133_10) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch133_6 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch133_6) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch133_16= tomMatch133_6.getAstName() ;if ( (tomMatch133_16 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch133_16) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.MAKE_APPEND,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() , tomMatch133_10.getString() , tomMatch133_16.getString() ,symbolType)

;
                 break matchblock;
               }}}}}}}}}}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {checkField(SyntaxCheckerPlugin.MAKE_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() ,symbolType)


;
                 break matchblock;
               }}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.code.types.BQTerm  tomMatch133_27= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getVarList() ; tom.engine.adt.code.types.BQTerm  tomMatch133_28= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getVarElt() ;if ( (tomMatch133_27 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch133_27) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch133_32= tomMatch133_27.getAstName() ;if ( (tomMatch133_32 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch133_32) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch133_28 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch133_28) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch133_38= tomMatch133_28.getAstName() ;if ( (tomMatch133_38 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch133_38) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(SyntaxCheckerPlugin.MAKE_INSERT,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() , tomMatch133_32.getString() , tomMatch133_38.getString() ,symbolType)

;
                 break matchblock;
               }}}}}}}}}}}}{if ( (tom_d instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom_d) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )tom_d)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomoption.types.Option  tomMatch133_46= (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getOrgTrack() ;if ( (tomMatch133_46 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch133_46) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {


                 if(!foundOpMake) {
                   foundOpMake = true;
                   verifyMakeDeclArgs( (( tom.engine.adt.tomdeclaration.types.Declaration )tom_d).getArgs() ,domainLength,tomMatch133_46,symbolType);
                 } else {
                   TomMessage.error(getLogger(),  tomMatch133_46.getFileName() ,  tomMatch133_46.getLine() , 
                       TomMessage.macroFunctionRepeated,
                       SyntaxCheckerPlugin.MAKE);
                 }
                 break matchblock;
               }}}}}}}

           }
      }}}if ( tomMatch132_end_4.isEmptyconcOption() ) {tomMatch132_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);} else {tomMatch132_end_4= tomMatch132_end_4.getTailconcOption() ;}}} while(!( (tomMatch132_end_4==(( tom.engine.adt.tomoption.types.OptionList )option)) ));}}}}

    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(symbolType, verifyList);
    }
  }  // verifySymbolMacroFunctions

  private void verifyMakeDeclArgs(BQTermList argsList, int domainLength, Option orgTrack, String symbolType) {
    // we test the necessity to use different names for each
    // variable-parameter.
    int nbArgs = 0;
    Collection<String> listVar = new HashSet<String>();
    {{if ( (argsList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )argsList)) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )argsList)) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch134_end_4=(( tom.engine.adt.code.types.BQTermList )argsList);do {{if (!( tomMatch134_end_4.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch134_8= tomMatch134_end_4.getHeadconcBQTerm() ;if ( (tomMatch134_8 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch134_8) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch134_7= tomMatch134_8.getAstName() ;if ( (tomMatch134_7 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch134_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch134_7.getString() ;
 // for each Macro variable
        if(listVar.contains(tom_name)) {
          TomMessage.error(getLogger(),
              orgTrack.getFileName(),orgTrack.getLine(),
              TomMessage.nonLinearMacroFunction,
              SyntaxCheckerPlugin.MAKE, tom_name);
        } else {
          listVar.add(tom_name);
        }
        nbArgs++;
      }}}}}if ( tomMatch134_end_4.isEmptyconcBQTerm() ) {tomMatch134_end_4=(( tom.engine.adt.code.types.BQTermList )argsList);} else {tomMatch134_end_4= tomMatch134_end_4.getTailconcBQTerm() ;}}} while(!( (tomMatch134_end_4==(( tom.engine.adt.code.types.BQTermList )argsList)) ));}}}}

    if(nbArgs != domainLength) {
      TomMessage.error(getLogger(),
          orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.badMakeDefinition,
          Integer.valueOf(nbArgs), Integer.valueOf(domainLength));
    }
  } // verifyMakeDeclArgs

  private void verifySymbolPairNameDeclList(PairNameDeclList pairNameDeclList, String symbolType) {
    // we test the existence of 2 same slot names
    Collection<String> listSlot = new HashSet<String>();
    {{if ( (pairNameDeclList instanceof tom.engine.adt.tomslot.types.PairNameDeclList) ) {if ( (((( tom.engine.adt.tomslot.types.PairNameDeclList )(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList)) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl) || ((( tom.engine.adt.tomslot.types.PairNameDeclList )(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList)) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl)) ) { tom.engine.adt.tomslot.types.PairNameDeclList  tomMatch135_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList);do {{if (!( tomMatch135_end_4.isEmptyconcPairNameDecl() )) { tom.engine.adt.tomslot.types.PairNameDecl  tomMatch135_8= tomMatch135_end_4.getHeadconcPairNameDecl() ;if ( (tomMatch135_8 instanceof tom.engine.adt.tomslot.types.PairNameDecl) ) {if ( ((( tom.engine.adt.tomslot.types.PairNameDecl )tomMatch135_8) instanceof tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch135_7= tomMatch135_8.getSlotName() ;if ( (tomMatch135_7 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch135_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch135_7.getString() ;
 // for each Slot
        if(listSlot.contains(tom_name)) {
          // TODO
          // messageWarningTwoSameSlotDeclError(name, orgTrack, symbolType);
        } else {
          listSlot.add(tom_name);
        }
      }}}}}if ( tomMatch135_end_4.isEmptyconcPairNameDecl() ) {tomMatch135_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList);} else {tomMatch135_end_4= tomMatch135_end_4.getTailconcPairNameDecl() ;}}} while(!( (tomMatch135_end_4==(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList)) ));}}}}

  } // verifySymbolPairNameDeclList

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
  } // messageMissingMacroFunctions

  // ////////////////////////////// /
  // MATCH VERIFICATION CONCERNS ///
  // ////////////////////////////////
  /**
   * Verifies the match construct
   * 
   * 0. checks that are not any circular dependencies
   * 1. Verifies all MatchConstraints
   * 2. Verifies all NumericConstraints 
   * 3. Verifies that in an OrConstraint, all the members have the same free variables
   */
  private void verifyMatch(ConstraintInstructionList constraintInstructionList, OptionList optionList) throws VisitFailure {

    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(optionList));
    Collection<Constraint> constraints = new HashSet<Constraint>();    
    Map<TomName, Collection<TomName>> varRelationsMap = new HashMap<TomName, Collection<TomName>>();
    Map<Constraint, Instruction> orActionMap = new HashMap<Constraint, Instruction>();
    Collection tmp = new HashSet();
    tom_make_TopDownCollect(tom_make_CollectConstraints(constraints,tmp,orActionMap)).visitLight(constraintInstructionList);
    for(Constraint constr: constraints) {
matchLbl: {{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constr)) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() ; tom.engine.adt.code.types.BQTerm  tom_subject= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ; tom.engine.adt.tomtype.types.TomType  tom_astType= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getAstType() ;

              Collection<TomName> patternVars = new HashSet<TomName>();
              Collection<TomName> subjectVars = new HashSet<TomName>();
              tom_make_TopDownCollect(tom_make_CollectVariables(patternVars)).visitLight(tom_pattern);
              tom_make_TopDownCollect(tom_make_CollectVariables(subjectVars)).visitLight(tom_subject);
              computeDependencies(varRelationsMap,patternVars,subjectVars);

              //System.out.println("varRelationsMap: " + varRelationsMap);
              //System.out.println("patternVars: " + patternVars);
              //System.out.println("subjectVars: " + subjectVars);

              if(tom_astType== SymbolTable.TYPE_UNKNOWN) {

                if (!getOptionBooleanValue("newtyper")) {//case of subtyping (-nt option activated)
                  // TODO: remove this test when newtyper will be the only typer
                  TomType typeMatch = getSubjectType(tom_subject,constraints);
                  if(typeMatch == null) {
                    {{if ( (tom_subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_subject)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch137_1= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstName() ;if ( (tomMatch137_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch137_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

                        TomMessage.error(getLogger(),
                            getCurrentTomStructureOrgTrack().getFileName(),
                            getCurrentTomStructureOrgTrack().getLine(),
                            TomMessage.cannotGuessMatchType,
                             tomMatch137_1.getString() );
                        return;
                      }}}}}}{if ( (tom_subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_subject)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch137_8= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstName() ;if ( (tomMatch137_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch137_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

                        TomMessage.error(getLogger(),
                            getCurrentTomStructureOrgTrack().getFileName(),
                            getCurrentTomStructureOrgTrack().getLine(),
                            TomMessage.cannotGuessMatchType,
                             tomMatch137_8.getString() );
                        return;
                      }}}}}}{if ( (tom_subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_subject)) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch137_15= (( tom.engine.adt.code.types.BQTerm )tom_subject).getAstName() ;if ( (tomMatch137_15 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch137_15) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

                        // do not throw an error message because Constant have no type
                      }}}}}}}

                    return;
                  }
                }
                verifyMatchPattern(tom_pattern, getPatternType(tom_pattern));
              } else {
                // astType is known
                if (!testTypeExistence(tom_astType.getTomType())) {
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.unknownType,
                      tom_astType.getTomType());
                }
                // we now compare the pattern to its definition
                verifyMatchPattern(tom_pattern, tom_astType);
              }
            }}}}{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constr)) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.code.types.BQTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getLeft() ; tom.engine.adt.code.types.BQTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getRight() ;









              // the lhs can only be a BQAppl, a BQVariable, or a BuildConstant
              {{if ( (tom_left instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch138_5= false ;boolean tomMatch138_6= false ; tom.engine.adt.code.types.BQTerm  tomMatch138_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch138_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch138_4= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom_left) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_left)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch138_6= true ;tomMatch138_2=(( tom.engine.adt.code.types.BQTerm )tom_left);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom_left) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_left)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch138_6= true ;tomMatch138_3=(( tom.engine.adt.code.types.BQTerm )tom_left);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom_left) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_left)) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{tomMatch138_6= true ;tomMatch138_4=(( tom.engine.adt.code.types.BQTerm )tom_left);}}}}}}}if (tomMatch138_6) {tomMatch138_5= true ;}if (!(tomMatch138_5)) {

                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.termOrVariableNumericLeft,
                      getName(tom_left));
                  return;
                }}}}
        
              // the rhs can only be a BQAppl, a BQVariable, or a BuildConstant
              {{if ( (tom_right instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch139_5= false ;boolean tomMatch139_6= false ; tom.engine.adt.code.types.BQTerm  tomMatch139_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch139_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch139_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom_right) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_right)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch139_6= true ;tomMatch139_2=(( tom.engine.adt.code.types.BQTerm )tom_right);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom_right) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_right)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch139_6= true ;tomMatch139_3=(( tom.engine.adt.code.types.BQTerm )tom_right);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom_right) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_right)) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{tomMatch139_6= true ;tomMatch139_4=(( tom.engine.adt.code.types.BQTerm )tom_right);}}}}}}}if (tomMatch139_6) {tomMatch139_5= true ;}if (!(tomMatch139_5)) {

                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.termOrVariableNumericRight,
                      getName(tom_right));
                  return;
                }}}}

              // if we have the type, check that it is the same
              TomType leftType = TomBase.getTermType(tom_left,getSymbolTable());
              TomType rightType = TomBase.getTermType(tom_right,getSymbolTable());
              // if the types are not available, leave the error to be raised by java
              if(leftType != null && leftType != SymbolTable.TYPE_UNKNOWN && leftType !=  tom.engine.adt.tomtype.types.tomtype.EmptyType.make()  
                  && rightType != null && rightType != SymbolTable.TYPE_UNKNOWN && rightType !=  tom.engine.adt.tomtype.types.tomtype.EmptyType.make()  
                  && (leftType != rightType)) {
                TomMessage.error(getLogger(),
                    getCurrentTomStructureOrgTrack().getFileName(),
                    getCurrentTomStructureOrgTrack().getLine(),
                    TomMessage.invalidTypesNumeric,
                    leftType,getName(tom_left),rightType,getName(tom_right)); 
                return;
              }
            }}}}{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constr)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constr)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_oc=(( tom.engine.adt.tomconstraint.types.Constraint )constr);


              if(!verifyOrConstraint(tom_oc,orActionMap.get(tom_oc))) {
                return;
              }
            }}}}

    } // for

    checkVarDependencies(varRelationsMap);
  } //verifyMatch

  /**
   * Puts all the variables in the list patternVars in relation with all the variables in subjectVars
   */
  private void computeDependencies(Map<TomName, Collection<TomName>> varRelationsMap, Collection<TomName> patternVars, Collection<TomName> subjectVars) {
    for(TomName x:patternVars) {      
      if(!varRelationsMap.keySet().contains(x)) {
        varRelationsMap.put(x,subjectVars);
      } else { // add the rest of the variables
        Collection<TomName> tmp = new HashSet<TomName>(subjectVars);
        tmp.addAll(varRelationsMap.get(x));
        varRelationsMap.put(x,tmp);
      }
    }
  }

  /**
   * Checks that there is not a circular reference of a variable
   */
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

  /**
   * Verifies that in an OrConstraint, all the members have the same free variables 
   * (only the match constraints have free variables - because only this type
   * of constraint can generate assignments)
   */
  private boolean verifyOrConstraint(Constraint orConstraint, Instruction action) throws VisitFailure {
    Collection<TomTerm> freeVarList1 = new HashSet<TomTerm>();
    Collection<TomTerm> freeVarList2 = new HashSet<TomTerm>();
    {{if ( (orConstraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch140_end_4=(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint);do {{if (!( (  tomMatch140_end_4.isEmptyOrConstraint()  ||  (tomMatch140_end_4== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) { tom.engine.adt.tomconstraint.types.Constraint  tom_x=(( ((tomMatch140_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (tomMatch140_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( tomMatch140_end_4.getHeadOrConstraint() ):(tomMatch140_end_4));

        // we collect the free vars only from match constraints
        // and we check these variables for numeric constraints also
        // ex: 'y << a() || x > 3' should generate an error 
        Strategy collect =  tom.library.sl.Sequence.make(tom_make_TopDownCollect(tom_make_CollectAliasVar(freeVarList2)), tom.library.sl.Sequence.make(tom_make_TopDownCollect(tom_make_CollectFreeVar(freeVarList2)), null ) ) 

;

        {{if ( (tom_x instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom_x) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom_x)) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 
            collect.visitLight( (( tom.engine.adt.tomconstraint.types.Constraint )tom_x).getPattern() );
          }}}}{if ( (tom_x instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom_x)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom_x)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch141_end_10=(( tom.engine.adt.tomconstraint.types.Constraint )tom_x);do {{if (!( (  tomMatch141_end_10.isEmptyAndConstraint()  ||  (tomMatch141_end_10== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch141_16=(( ((tomMatch141_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch141_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch141_end_10.getHeadAndConstraint() ):(tomMatch141_end_10));if ( (tomMatch141_16 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch141_16) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 
            collect.visitLight( tomMatch141_16.getPattern() );
          }}}if ( (  tomMatch141_end_10.isEmptyAndConstraint()  ||  (tomMatch141_end_10== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {tomMatch141_end_10=(( tom.engine.adt.tomconstraint.types.Constraint )tom_x);} else {tomMatch141_end_10=(( ((tomMatch141_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch141_end_10 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch141_end_10.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));}}} while(!( (tomMatch141_end_10==(( tom.engine.adt.tomconstraint.types.Constraint )tom_x)) ));}}}}

        //System.out.println("freeVar1 = " + freeVarList1);
        //System.out.println("freeVar2 = " + freeVarList2);

        if(!freeVarList1.isEmpty()) {
          for(TomTerm term:freeVarList2) {
            if(!containsVariable(term,freeVarList1)) {
              if(containsVariable(term, action)) {              
                String varName = term.getAstName().getString();
                TomMessage.error(getLogger(),
                    getCurrentTomStructureOrgTrack().getFileName(),
                    getCurrentTomStructureOrgTrack().getLine(),
                    TomMessage.freeVarNotPresentInOr,
                    varName);
                return false;
              }
            }
          }
          for(TomTerm term:freeVarList1) {
            if(!containsVariable(term,freeVarList2)) {
              if(containsVariable(term, action))  {
                String varName = term.getAstName().getString();
                TomMessage.error(getLogger(),
                    getCurrentTomStructureOrgTrack().getFileName(),
                    getCurrentTomStructureOrgTrack().getLine(),
                    TomMessage.freeVarNotPresentInOr,
                    varName);
                return false;
              }
            }
          }          
        }
        freeVarList1 = new HashSet<TomTerm>(freeVarList2);
        freeVarList2.clear();
      }if ( (  tomMatch140_end_4.isEmptyOrConstraint()  ||  (tomMatch140_end_4== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) ) {tomMatch140_end_4=(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint);} else {tomMatch140_end_4=(( ((tomMatch140_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (tomMatch140_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( tomMatch140_end_4.getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ));}}} while(!( (tomMatch140_end_4==(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint)) ));}}}}

    return true;
  }

  private boolean containsVariable(TomTerm var, Collection<TomTerm> list) {
    for(TomTerm t:list) {
      {{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( (t instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )t) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )t)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() == (( tom.engine.adt.tomterm.types.TomTerm )t).getAstName() ) ) {
 return true; }}}}}}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( (t instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )t) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )t)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() == (( tom.engine.adt.tomterm.types.TomTerm )t).getAstName() ) ) {
 return true; }}}}}}}}}

    }
    return false;
  }

  private boolean containsVariable(TomTerm var, Instruction action) {
    try {
      tom_make_TopDown(tom_make_ContainsVariable(var)).visitLight(action);
    } catch(VisitFailure e) {
      return true;
    }
    return false;
  }

  public static class ContainsVariable extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomterm.types.TomTerm  var;public ContainsVariable( tom.engine.adt.tomterm.types.TomTerm  var) {super(( new tom.library.sl.Identity() ));this.var=var;}public  tom.engine.adt.tomterm.types.TomTerm  getvar() {return var;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch143_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch143_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch143_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch143_4= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch143_5= true ;tomMatch143_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch143_1= tomMatch143_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch143_5= true ;tomMatch143_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch143_1= tomMatch143_4.getAstName() ;}}}}}if (tomMatch143_5) {


        //System.out.println("name = " + `name);
        //System.out.println("var.name = " + var.getAstName());
        if(tomMatch143_1==var.getAstName()) {
          throw new VisitFailure();
        }
      }}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ContainsVariable( tom.engine.adt.tomterm.types.TomTerm  t0) { return new ContainsVariable(t0);}



  /**
   * Collect the free variables in an constraint (do not inspect under a anti)  
   */
  public static class CollectFreeVar extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectFreeVar( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch144_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch144_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch144_2= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch144_4= true ;tomMatch144_2=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch144_4= true ;tomMatch144_3=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}}}}}if (tomMatch144_4) { tom.engine.adt.tomterm.types.TomTerm  tom_v=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);


        TomTerm newvar = tom_v.setOptions( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ); // to avoid problems related to line numbers
        if(!varList.contains(newvar)) { 
          varList.add(tom_v); 
        }
        throw new VisitFailure();/* stop the top-down */
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
        
        throw new VisitFailure();/* stop the top-down */
      }}}}}return _visit_TomTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectFreeVar( java.util.Collection  t0) { return new CollectFreeVar(t0);}



  /**
   * Collect the anoted variables in an constraint (should inspect under a anti)  
   */
  public static class CollectAliasVar extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectAliasVar( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch145_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getVar() ;boolean tomMatch145_7= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch145_6= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch145_5= null ;if ( (tomMatch145_1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch145_1) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch145_7= true ;tomMatch145_5=tomMatch145_1;}} else {if ( (tomMatch145_1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch145_1) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch145_7= true ;tomMatch145_6=tomMatch145_1;}}}}}if (tomMatch145_7) { tom.engine.adt.tomterm.types.TomTerm  tom_v=tomMatch145_1;


        TomTerm newvar = tom_v.setOptions( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ); // to avoid problems related to line numbers
        if(!varList.contains(newvar)) { 
          varList.add(tom_v); 
        }
        throw new VisitFailure();/* stop the top-down */
      }}}}}}return _visit_Constraint(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectAliasVar( java.util.Collection  t0) { return new CollectAliasVar(t0);}



  /**
   * Collect the variables' names   
   */
  public static class CollectVariables extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectVariables( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch146_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch146_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch146_4= null ; tom.engine.adt.tomname.types.TomName  tomMatch146_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch146_5= true ;tomMatch146_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch146_1= tomMatch146_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch146_5= true ;tomMatch146_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch146_1= tomMatch146_4.getAstName() ;}}}}}if (tomMatch146_5) { tom.engine.adt.tomname.types.TomName  tom_name=tomMatch146_1;









        
        if(!varList.contains(tom_name)) {
          varList.add(tom_name); 
        }
        throw new VisitFailure();/* stop the top-down */
      }}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch147_5= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch147_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch147_1= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch147_4= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch147_5= true ;tomMatch147_3=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);tomMatch147_1= tomMatch147_3.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch147_5= true ;tomMatch147_4=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);tomMatch147_1= tomMatch147_4.getAstName() ;}}}}}if (tomMatch147_5) { tom.engine.adt.tomname.types.TomName  tom_name=tomMatch147_1;                 if(!varList.contains(tom_name)) {           varList.add(tom_name);          }         throw new VisitFailure();/* stop the top-down */       }}}}return _visit_TomTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectVariables( java.util.Collection  t0) { return new CollectVariables(t0);}



  /**
   * tries to give the type of the tomTerm received as parameter
   */
  private TomType getSubjectType(BQTerm subject, Collection<Constraint> constraints) {
    {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch148_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch148_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch148_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
        
        try {
          Integer.parseInt( tomMatch148_1.getString() );
          return getSymbolTable().getIntType();
        } catch(java.lang.NumberFormatException e) {
          return getSymbolTable().getStringType();
        }
      }}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch148_8= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch148_9= (( tom.engine.adt.code.types.BQTerm )subject).getAstType() ;if ( (tomMatch148_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch148_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch148_9 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch148_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch148_17= tomMatch148_9.getTlType() ; String  tom_type= tomMatch148_9.getTomType() ;if ( (tomMatch148_17 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch148_17) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) { tom.engine.adt.tomtype.types.TomType  tom_tomType=tomMatch148_9;

 
        if(tom_tomType==SymbolTable.TYPE_UNKNOWN) {
          // try to guess
          return guessSubjectType(subject,constraints);
        } else if(testTypeExistence(tom_type)) {
          return tom_tomType;
        } else {
          TomMessage.error(getLogger(),
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.unknownMatchArgumentTypeInSignature,
               tomMatch148_8.getString() , tom_type);                
        }
      }}}}}}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch148_23= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch148_23 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch148_23) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch148_23.getString() ;


        TomSymbol symbol = getSymbolFromName(tom_name);
        if(symbol!=null) {
          TomType type = TomBase.getSymbolCodomain(symbol);
          String typeName = TomBase.getTomType(type);
          if(!testTypeExistence(typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                tom_name, typeName);
          }          
          //verifyBQAppl(`term);
          return type;
        } else {
          // try to guess
          return guessSubjectType(subject,constraints);
        }
      }}}}}}}

    return null;
  }

  private TomType getPatternType(TomTerm pattern) {
    {{if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )pattern)) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 pattern =  (( tom.engine.adt.tomterm.types.TomTerm )pattern).getTomTerm() ; }}}}}{{if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch150_12= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch150_4= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch150_5= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch150_3= null ; tom.engine.adt.tomname.types.TomNameList  tomMatch150_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )pattern)) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch150_12= true ;tomMatch150_3=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch150_1= tomMatch150_3.getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )pattern)) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch150_12= true ;tomMatch150_4=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch150_1= tomMatch150_4.getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )pattern)) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {{tomMatch150_12= true ;tomMatch150_5=(( tom.engine.adt.tomterm.types.TomTerm )pattern);tomMatch150_1= tomMatch150_5.getNameList() ;}}}}}}}if (tomMatch150_12) {if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch150_1) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch150_1) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch150_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch150_10= tomMatch150_1.getHeadconcTomName() ;if ( (tomMatch150_10 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch150_10) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch150_10.getString() ;


        
        TomSymbol symbol = null;
        if(pattern.isXMLAppl()) {
          symbol = getSymbolFromName(Constants.ELEMENT_NODE);
        } else {
          symbol = getSymbolFromName(tom_name);
        }                
        if(symbol!=null) {
          TomType type = TomBase.getSymbolCodomain(symbol);
          // System.out.println("type = " + type);            
          String typeName = TomBase.getTomType(type);
          if(!testTypeExistence(typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                tom_name, typeName);
          }
          return type;
        }
      }}}}}}}}





















    return null;
  }

  /**
   * if a type is not specified 
   * 1. we look for a type in all match constraints where we can find this subject
   * 2. TODO: if the subject is in a constraint with a variable (the pattern is a variable for instance),
   * try to see if a variable with the same name already exists and can be typed, and if yes, get that type
   */
  private TomType guessSubjectType(BQTerm subject,Collection<Constraint> constraints) {
    for(Constraint constr:constraints) {
      {{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constr)) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.code.types.BQTerm  tom_s= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ;

          // we want two terms to be equal even if their option is different 
          //( because of their position for example )
matchL:  {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {if ( (tom_s instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_s) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_s)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() == (( tom.engine.adt.code.types.BQTerm )tom_s).getAstName() ) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstType() == (( tom.engine.adt.code.types.BQTerm )tom_s).getAstType() ) ) {
break matchL;}}}}}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {if ( (tom_s instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_s) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom_s)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() == (( tom.engine.adt.code.types.BQTerm )tom_s).getAstName() ) ) {if ( ( (( tom.engine.adt.code.types.BQTerm )subject).getArgs() == (( tom.engine.adt.code.types.BQTerm )tom_s).getArgs() ) ) {
break matchL;}}}}}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (tom_s instanceof tom.engine.adt.code.types.BQTerm) ) {
 continue; }}}}


         TomType type = getPatternType( (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() );
         if(type!=null) {
           return type;
         }
        }}}}}

    }// for    
    return null;
  }

  //  /**
  //   * trys to guess the type of the variable by looking into all constraints 
  //   * if it can find it somewhere where it is typed
  //   */
  //  private TomType guessVarTypeFromConstraints(TomTerm var, ArrayList<Constraint> constraints){
  //    
  //  }

  /**
   * Collect the matchConstraints in a list of constraints   
   */
  public static class CollectMatchConstraints extends tom.library.sl.AbstractStrategyBasic {private  java.util.List  constrList;public CollectMatchConstraints( java.util.List  constrList) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;}public  java.util.List  getconstrList() {return constrList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {

        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));         
        throw new VisitFailure();/* stop the top-down */
      }}}}}return _visit_Constraint(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectMatchConstraints( java.util.List  t0) { return new CollectMatchConstraints(t0);}



  /**
   * Collect the constraints (match and numeric)
   * For Or constraints, collect also the associated action
   */
  public static class CollectConstraints extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  constrList;private  java.util.Collection  lastAction;private  java.util.Map  orActionMap;public CollectConstraints( java.util.Collection  constrList,  java.util.Collection  lastAction,  java.util.Map  orActionMap) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;this.lastAction=lastAction;this.orActionMap=orActionMap;}public  java.util.Collection  getconstrList() {return constrList;}public  java.util.Collection  getlastAction() {return lastAction;}public  java.util.Map  getorActionMap() {return orActionMap;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {return ((T)visit_ConstraintInstruction((( tom.engine.adt.tominstruction.types.ConstraintInstruction )v),introspector));}if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  _visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.ConstraintInstruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {boolean tomMatch154_4= false ; tom.engine.adt.tomconstraint.types.Constraint  tomMatch154_3= null ; tom.engine.adt.tomconstraint.types.Constraint  tomMatch154_2= null ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {{tomMatch154_4= true ;tomMatch154_2=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);}} else {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {{tomMatch154_4= true ;tomMatch154_3=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);}}}}}if (tomMatch154_4) {







        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));         
        throw new VisitFailure();/* stop the top-down */
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_oc=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);

        constrList.add(tom_oc);
        Iterator it = lastAction.iterator();
        orActionMap.put(tom_oc,it.next());
      }}}}return _visit_Constraint(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )(( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg)) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {         lastAction.clear();         lastAction.add( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getAction() );       }}}}}return _visit_ConstraintInstruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectConstraints( java.util.Collection  t0,  java.util.Collection  t1,  java.util.Map  t2) { return new CollectConstraints(t0,t1,t2);}



  /**
   * check the lhs of a rule
   */
  private void verifyMatchPattern(TomTerm term, TomType type) {      
    // the term cannot be a Var* nor a _*
    
    //System.out.println("verifyMatchPattern: " + term);

    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch156_2= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( (tomMatch156_2 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch156_2) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        String fileName = findOriginTrackingFileName(tom_options);
        int decLine = findOriginTrackingLine(tom_options);
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.incorrectVariableStarInMatch, "_*");
        return;
      }}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch156_9= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( (tomMatch156_9 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch156_9) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        String fileName = findOriginTrackingFileName(tom_options);
        int decLine = findOriginTrackingLine(tom_options);
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.incorrectVariableStarInMatch,  tomMatch156_9.getString() );
        return;
      }}}}}}}

    // Analyse the term if type != null
    //if(type != null) {
      // the type is known and found in the match signature
    // 'type' may be null
      validateTerm(term, type, false, true);
    //}
  }

  /*
   * verify the structure of a %strategy
   */
  private void verifyStrategy(TomVisitList visitList) throws VisitFailure {
    {{if ( (visitList instanceof tom.engine.adt.tomsignature.types.TomVisitList) ) {if ( (((( tom.engine.adt.tomsignature.types.TomVisitList )(( tom.engine.adt.tomsignature.types.TomVisitList )visitList)) instanceof tom.engine.adt.tomsignature.types.tomvisitlist.ConsconcTomVisit) || ((( tom.engine.adt.tomsignature.types.TomVisitList )(( tom.engine.adt.tomsignature.types.TomVisitList )visitList)) instanceof tom.engine.adt.tomsignature.types.tomvisitlist.EmptyconcTomVisit)) ) { tom.engine.adt.tomsignature.types.TomVisitList  tomMatch157_end_4=(( tom.engine.adt.tomsignature.types.TomVisitList )visitList);do {{if (!( tomMatch157_end_4.isEmptyconcTomVisit() )) { tom.engine.adt.tomsignature.types.TomVisit  tomMatch157_10= tomMatch157_end_4.getHeadconcTomVisit() ;if ( (tomMatch157_10 instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tomMatch157_10) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= tomMatch157_10.getAstConstraintInstructionList() ;

        List<MatchConstraint> matchConstraints = new ArrayList<MatchConstraint>();
        {{if ( (tom_constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList)) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList)) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tomMatch158_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList);do {{if (!( tomMatch158_end_4.isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch158_8= tomMatch158_end_4.getHeadconcConstraintInstruction() ;if ( (tomMatch158_8 instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tomMatch158_8) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {

            matchConstraints.clear();
            tom_make_TopDownCollect(tom_make_CollectMatchConstraints(matchConstraints)).visitLight( tomMatch158_8.getConstraint() );   
            // for the first constraint, check that the type is conrform to the type specified in visit
            // the order is important, this is why we used an ArrayList.
            // TopDown is supposed to traverse from left-to-right
            MatchConstraint firstMatchConstr = matchConstraints.iterator().next(); 
            verifyMatchPattern(firstMatchConstr.getPattern(),  tomMatch157_10.getVNode() );
          }}}if ( tomMatch158_end_4.isEmptyconcConstraintInstruction() ) {tomMatch158_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList);} else {tomMatch158_end_4= tomMatch158_end_4.getTailconcConstraintInstruction() ;}}} while(!( (tomMatch158_end_4==(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList)) ));}}}}
    
        // check the rest of the constraints
        verifyMatch(tom_constraintInstructionList, tomMatch157_10.getOptions() );
      }}}if ( tomMatch157_end_4.isEmptyconcTomVisit() ) {tomMatch157_end_4=(( tom.engine.adt.tomsignature.types.TomVisitList )visitList);} else {tomMatch157_end_4= tomMatch157_end_4.getTailconcTomVisit() ;}}} while(!( (tomMatch157_end_4==(( tom.engine.adt.tomsignature.types.TomVisitList )visitList)) ));}}}}

  }

  /*
   * Verify structure of a %transformation
   */
//  private void verifyTransformation(TomWithToList wtlist) throws VisitFailure {
    //TODO:
    // - test if a withTerm exists (+args) -> here ?
    // - test toTerm structure ?
//  }

  /**
   * Analyse a term given an expected type and re-enter recursively on children
   */
  public TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel) {
    String termName = "emptyName";
    TomType type = null;
    int termClass = -1;
    String fileName = "unknown";
    int decLine = -1;
    Option orgTrack;

    //System.out.println("validateTerm: " + term);

matchblock:{
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ; tom.engine.adt.tomname.types.TomNameList  tom_symbolNameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomterm.types.TomList  tom_arguments= (( tom.engine.adt.tomterm.types.TomTerm )term).getArgs() ;

        fileName = findOriginTrackingFileName(tom_options);
        decLine = findOriginTrackingLine(tom_options);
        termClass = TERM_APPL;

        TomSymbol symbol = ensureValidApplDisjunction(tom_symbolNameList, expectedType, fileName, decLine,  topLevel);

        if(symbol == null) {
          // null means that an error occured
          break matchblock;
        }
        // Type is OK
        type = expectedType;
        TomName headName = tom_symbolNameList.getHeadconcTomName();
        if(headName instanceof AntiName) {
          headName = ((AntiName)headName).getName();
        }
        termName = headName.getString();
        boolean listOp = (TomBase.isListOperator(symbol) || TomBase.isArrayOperator(symbol));
        if(listOp) {
          // whatever the arity is, we continue recursively and there is
          // only one element in the Domain
          // - we can also have children that are sublists
          validateListOperatorArgs(tom_arguments, symbol.getTypesToType().getDomain().getHeadconcTomType(),
              symbol.getTypesToType().getCodomain());
        } else {
          // the arity is important also there are different types in Domain
          TomTypeList types = symbol.getTypesToType().getDomain();
          int nbArgs = tom_arguments.length();
          int nbExpectedArgs = types.length();
          if(nbArgs != nbExpectedArgs) {
            TomMessage.error(getLogger(),
                fileName, decLine, TomMessage.symbolNumberArgument,
                termName, Integer.valueOf(nbExpectedArgs), Integer.valueOf(nbArgs));
            break matchblock;
          }
          TomList args = tom_arguments;
          while(!args.isEmptyconcTomTerm()) {
            // repeat analyse with associated expected type and control
            // arity
            validateTerm(args.getHeadconcTomTerm(), types.getHeadconcTomType(), listOp/* false */, false);
            args = args.getTailconcTomTerm();
            types = types.getTailconcTomType();
          }
        }
        break matchblock;
      }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ; tom.engine.adt.tomname.types.TomNameList  tom_symbolNameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomslot.types.SlotList  tom_slotList= (( tom.engine.adt.tomterm.types.TomTerm )term).getSlots() ;


        fileName = findOriginTrackingFileName(tom_options);
        decLine = findOriginTrackingLine(tom_options);
        termClass = RECORD_APPL;
        TomSymbol symbol = ensureValidRecordDisjunction(tom_symbolNameList, tom_slotList, expectedType, fileName, decLine, true);
        if(symbol == null) {
          break matchblock;
        }

        {{if ( (tom_symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList)) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList)) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch160_end_4=(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList);do {{if (!( tomMatch160_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch160_8= tomMatch160_end_4.getHeadconcTomName() ;if ( (tomMatch160_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch160_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {





            verifyRecordStructure(tom_options,  tomMatch160_8.getString() , tom_slotList, fileName,decLine);
          }}}if ( tomMatch160_end_4.isEmptyconcTomName() ) {tomMatch160_end_4=(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList);} else {tomMatch160_end_4= tomMatch160_end_4.getTailconcTomName() ;}}} while(!( (tomMatch160_end_4==(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList)) ));}}}}


        type = expectedType;
        TomName headName = tom_symbolNameList.getHeadconcTomName();
        if(headName instanceof AntiName) {
          headName = ((AntiName)headName).getName();
        }
        termName = headName.getString();
        break matchblock;
      }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch159_14= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch159_14) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch159_14) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch159_end_21=tomMatch159_14;do {{if (!( tomMatch159_end_21.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch159_25= tomMatch159_end_21.getHeadconcTomName() ;if ( (tomMatch159_25 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch159_25) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        // TODO: can we do it
        // ensureValidDisjunction(symbolNameList); ??????????
        termClass = XML_APPL;
        fileName = findOriginTrackingFileName(tom_options);
        decLine = findOriginTrackingLine(tom_options);
        type = TomBase.getSymbolCodomain(getSymbolFromName(Constants.ELEMENT_NODE));
        termName = Constants.ELEMENT_NODE;

        TomList args =  (( tom.engine.adt.tomterm.types.TomTerm )term).getChildList() ;
        /*
         * we cannot use the following expression TomType TNodeType =
         * getSymbolTable().getType(Constants.TNODE); because TNodeType should be
         * a TomTypeAlone and not an expanded type
         */
        TomType TNodeType = TomBase.getSymbolCodomain(getSymbolTable().getSymbolFromName(Constants.ELEMENT_NODE));
        // System.out.println("TNodeType = " + TNodeType);
        while(!args.isEmptyconcTomTerm()) {
          // repeat analyse with associated expected type and control arity
          validateTerm(args.getHeadconcTomTerm(), TNodeType, true, false);
          args = args.getTailconcTomTerm();
        }

        break matchblock;
      }}}if ( tomMatch159_end_21.isEmptyconcTomName() ) {tomMatch159_end_21=tomMatch159_14;} else {tomMatch159_end_21= tomMatch159_end_21.getTailconcTomName() ;}}} while(!( (tomMatch159_end_21==tomMatch159_14) ));}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch159_29= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( (tomMatch159_29 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch159_29) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        termClass = VARIABLE;
        fileName = findOriginTrackingFileName(tom_options);
        decLine = findOriginTrackingLine(tom_options);
        type = null;
        termName =  tomMatch159_29.getString() ;
        break matchblock;
      }}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch159_37= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( (tomMatch159_37 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch159_37) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        termClass = VARIABLE_STAR;
        fileName = findOriginTrackingFileName(tom_options);
        decLine = findOriginTrackingLine(tom_options);
        type = null;
        termName =  tomMatch159_37.getString() +"*";
        if(!listSymbol) {
          TomMessage.error(getLogger(),
              fileName,
              decLine,
              TomMessage.invalidVariableStarArgument,
              termName);
        }
        break matchblock;
      }}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch159_45= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( (tomMatch159_45 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch159_45) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        termClass = UNAMED_VARIABLE;
        fileName = findOriginTrackingFileName(tom_options);
        decLine = findOriginTrackingLine(tom_options);
        type = null;
        termName = "unamed";
        break matchblock;
      }}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch159_52= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOptions() ;if ( (tomMatch159_52 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch159_52) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {


        termClass = UNAMED_VARIABLE_STAR;
        fileName = findOriginTrackingFileName(tom_options);
        decLine = findOriginTrackingLine(tom_options);
        type = null;
        termName = "unamed*";
        if(!listSymbol) {
          TomMessage.error(getLogger(),
              fileName,
              decLine,
              TomMessage.invalidVariableStarArgument,
              termName);
        }
        break matchblock;
      }}}}}}}


    throw new TomRuntimeException("Strange Term "+term);
    } // end matchblock
    return new TermDescription(termClass, termName, fileName,decLine, type);
  }

  private TomSymbol ensureValidApplDisjunction(TomNameList symbolNameList, TomType expectedType, 
      String fileName, int decLine,  boolean topLevel) {

    //System.out.println("symbolNameList = " + symbolNameList);

    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);

      //System.out.println("symbol = " + symbol);

      if(symbol == null ) {
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbol, res);
        return null;
      } else { // known symbol
        if (!getOptionBooleanValue("newtyper")) {//case of subtyping (-nt option activated)
          if( !topLevel ) {
            if(!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
              return null;
            }
          }
        }
      }
      return symbol;
    } else {
      // this is a disjunction
      TomSymbol symbol = null;
      TomTypeList domainReference = null;
      PairNameDeclList slotReference = null;
      String nameReference = null;
      {{if ( (symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch161_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);do {{if (!( tomMatch161_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch161_8= tomMatch161_end_4.getHeadconcTomName() ;if ( (tomMatch161_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch161_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_dijName= tomMatch161_8.getString() ;
 // for each SymbolName
          symbol = getSymbolFromName(tom_dijName);
          if(symbol == null) {
            TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbolInDisjunction,tom_dijName);
            return null;
          }
          if( !topLevel ) {
            // ensure codomain is correct
            if(!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, tom_dijName, fileName,decLine)) {
              return null;
            }
          }

          if(domainReference == null) { // save Domain reference
            domainReference = TomBase.getSymbolDomain(symbol);
            slotReference = symbol.getPairNameDeclList();
            nameReference = tom_dijName;
          } else {
            if(TomBase.getSymbolDomain(symbol) != domainReference) {
              TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, nameReference, tom_dijName);
              return null;
            }
            if(symbol.getPairNameDeclList() != slotReference) {
              PairNameDeclList l1 = slotReference;
              PairNameDeclList l2 = symbol.getPairNameDeclList();
              while(!l1.isEmptyconcPairNameDecl()) {
                if(l1.getHeadconcPairNameDecl().getSlotName() != l2.getHeadconcPairNameDecl().getSlotName()) {
                  TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, nameReference, tom_dijName);
                  return null;
                }
                l1=l1.getTailconcPairNameDecl();
                l2=l2.getTailconcPairNameDecl();
              }
            }
          }
        }}}if ( tomMatch161_end_4.isEmptyconcTomName() ) {tomMatch161_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);} else {tomMatch161_end_4= tomMatch161_end_4.getTailconcTomName() ;}}} while(!( (tomMatch161_end_4==(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) ));}}}}

      return symbol;
    }
  } //ensureValidApplDisjunction

  private boolean ensureSymbolCodomain(TomType currentCodomain, TomType expectedType, TomMessage msg, String symbolName, String fileName,int decLine) {
    {{if ( (currentCodomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )currentCodomain) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )currentCodomain)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( (expectedType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )expectedType) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )expectedType)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  (( tom.engine.adt.tomtype.types.TomType )currentCodomain).getTomType() .equals( (( tom.engine.adt.tomtype.types.TomType )expectedType).getTomType() ) ) {

        return true;
      }}}}}}}}}

    //System.out.println(currentCodomain+"!="+expectedType);
    TomMessage.error(getLogger(),fileName,decLine, msg,
        symbolName, currentCodomain.getTomType(), expectedType.getTomType());
    return false;
  } //ensureSymbolCodomain

  private TomSymbol ensureValidRecordDisjunction(TomNameList symbolNameList, SlotList slotList, 
      TomType expectedType, String fileName, int decLine, boolean topLevel) {
    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);
      if (symbol == null ) { // this correspond to: unknown[]
        // it is not correct to use Record with unknown symbols
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbol, res);
        return null;
      } else { // known symbol
        // ensure type correctness if necessary
        if (!getOptionBooleanValue("newtyper")) {//case of subtyping (-nt option activated)
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
      {{if ( (symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch163_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);do {{if (!( tomMatch163_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch163_8= tomMatch163_end_4.getHeadconcTomName() ;if ( (tomMatch163_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch163_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_dijName= tomMatch163_8.getString() ;
 // for each SymbolName
          symbol =  getSymbolFromName(tom_dijName);
          if(symbol == null) {
            // In disjunction we can only have known symbols
            TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbolInDisjunction, tom_dijName);
            return null;
          }
          if( !topLevel ) {
            // ensure codomain is correct
            if (!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, tom_dijName, fileName,decLine)) {
              return null;
            }
          }
          // System.out.println("domain = " + getSymbolDomain(symbol));

          if(referenceDomain == null) { // save Domain reference
            referenceSymbol = symbol;
            referenceName = tom_dijName;
            referenceDomain = TomBase.getSymbolDomain(symbol);
          } else {
            // check that domains are compatible
            TomTypeList currentDomain = TomBase.getSymbolDomain(symbol);
            // restrict the domain to the record
            while(!slotList.isEmptyconcSlot()) {
              Slot slot = slotList.getHeadconcSlot();
              TomName slotName = slot.getSlotName();
              int currentSlotIndex = TomBase.getSlotIndex(symbol,slotName);
              int referenceSlotIndex = TomBase.getSlotIndex(referenceSymbol,slotName);

              // System.out.println("index1 = " + currentSlotIndex);
              // System.out.println("type1 = " +
              // TomBase.elementAt(currentDomain,currentSlotIndex));
              // System.out.println("index2 = " + referenceSlotIndex);
              // System.out.println("type2 = " +
              // TomBase.elementAt(referenceDomain,referenceSlotIndex));

              if (referenceSlotIndex == -1){
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionSlotName, referenceName,((Name)slotName).getString());
                return null;                
              }

              if (currentSlotIndex == -1){
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionSlotName, tom_dijName,((Name)slotName).getString());
                return null;                
              }

              if(TomBase.elementAt(currentDomain,currentSlotIndex) != TomBase.elementAt(referenceDomain,referenceSlotIndex)) {
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, referenceName, tom_dijName);
                return null;
              }

              slotList = slotList.getTailconcSlot();
            }

          }
        }}}if ( tomMatch163_end_4.isEmptyconcTomName() ) {tomMatch163_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);} else {tomMatch163_end_4= tomMatch163_end_4.getTailconcTomName() ;}}} while(!( (tomMatch163_end_4==(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) ));}}}}

      return symbol;
    }
  } //ensureValidRecordDisjunction

  // /////////////////////
  // RECORDS CONCERNS ///
  // /////////////////////
  private void verifyRecordStructure(OptionList option, String tomName, SlotList slotList, String fileName, int decLine)  {
    TomSymbol symbol = getSymbolFromName(tomName);
    if(symbol != null) {
      // constants have an emptyPairNameDeclList
      // the length of the pairNameDeclList corresponds to the arity of the
      // operator

      // Note: cannot detect conc[], because the concrete syntax [] is no longer there
      //if(slotList.isEmptyconcSlot() && (TomBase.isListOperator(symbol) ||  TomBase.isArrayOperator(symbol)) ) {
      //  TomMessage.error(getLogger(),fileName,decLine, TomMessage.bracketOnListSymbol, tomName);
      //}
      // TODO verify type
      verifyRecordSlots(slotList,symbol, TomBase.getSymbolDomain(symbol), tomName, fileName, decLine);
    } else {
      TomMessage.error(getLogger(),fileName,decLine,
          TomMessage.unknownSymbol,
          tomName);
    }
  } //verifyRecordStructure

  // We test the existence/repetition of slotName contained in pairSlotAppl
  // and then the associated term
  private void verifyRecordSlots(SlotList slotList, TomSymbol tomSymbol, TomTypeList typeList, String methodName, String fileName, int decLine) {
    //System.out.println("verifyRecordSlot: " + `slotList);
    Collection<String> listOfPossibleSlot = null;
    Collection<Integer> studiedSlotIndexList = new HashSet<Integer>();
    // for each pair slotName <=> Appl
    while( !slotList.isEmptyconcSlot() ) {
      TomName pairSlotName = slotList.getHeadconcSlot().getSlotName();
      // First check for slot name correctness
      int index = TomBase.getSlotIndex(tomSymbol,pairSlotName);
      if(index < 0) {// Error: bad slot name
        if(listOfPossibleSlot == null) {
          // calculate list of possible slot names..
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
        return; // break analyses
      } else { // then check for repeated good slot name
        //Integer integerIndex = Integer.valueOf(index);
        if(pairSlotName.isName() && studiedSlotIndexList.contains(index)) {
          // we are not insterrested in EmptyName (which come from ListOperator with Empty-SlotName)
          // Error: repeated slot
          TomMessage.error(getLogger(),fileName,decLine,
              TomMessage.slotRepeated,
              methodName, pairSlotName.getString());
          return; // break analyses
        }
        studiedSlotIndexList.add(index);
      }

      // Now analyses associated term
      PairNameDeclList listOfSlots =  tomSymbol.getPairNameDeclList();
      TomTypeList listOfTypes = typeList;
      while(!listOfSlots.isEmptyconcPairNameDecl()) {
        SlotList listOfPair = slotList;
        TomName slotName = listOfSlots.getHeadconcPairNameDecl().getSlotName();
        TomType expectedType = listOfTypes.getHeadconcTomType();
        if(!slotName.isEmptyName()) {
          // look for a same name (from record)
whileBlock: {
              while(!listOfPair.isEmptyconcSlot()) {
                Slot pairSlotTerm = listOfPair.getHeadconcSlot();
                {{if ( (slotName instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )slotName) instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )(( tom.engine.adt.tomname.types.TomName )slotName)) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (pairSlotTerm instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )pairSlotTerm) instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )(( tom.engine.adt.tomslot.types.Slot )pairSlotTerm)) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch164_5= (( tom.engine.adt.tomslot.types.Slot )pairSlotTerm).getSlotName() ;if ( (tomMatch164_5 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch164_5) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  (( tom.engine.adt.tomname.types.TomName )slotName).getString() .equals( tomMatch164_5.getString() ) ) {

                    validateTerm( (( tom.engine.adt.tomslot.types.Slot )pairSlotTerm).getAppl() ,expectedType, false, true);
                    break whileBlock;
                  }}}}}}}}}}{if ( (slotName instanceof tom.engine.adt.tomname.types.TomName) ) {if ( (pairSlotTerm instanceof tom.engine.adt.tomslot.types.Slot) ) {
listOfPair = listOfPair.getTailconcSlot();}}}}

              }
            }
        }
        // prepare next step
        listOfSlots = listOfSlots.getTailconcPairNameDecl();
        listOfTypes = listOfTypes.getTailconcTomType();
      }

      slotList = slotList.getTailconcSlot();
    }
  } //verifyRecordSlots

  public void validateListOperatorArgs(TomList args, TomType expectedType, TomType parentListCodomain) {
    {{if ( (args instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )args)) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )args)) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch165_end_4=(( tom.engine.adt.tomterm.types.TomList )args);do {{if (!( tomMatch165_end_4.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch165_7= tomMatch165_end_4.getHeadconcTomTerm() ;boolean tomMatch165_11= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch165_10= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch165_8= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch165_9= null ;if ( (tomMatch165_7 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch165_7) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch165_11= true ;tomMatch165_8=tomMatch165_7;}} else {if ( (tomMatch165_7 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch165_7) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch165_11= true ;tomMatch165_9=tomMatch165_7;}} else {if ( (tomMatch165_7 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch165_7) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {{tomMatch165_11= true ;tomMatch165_10=tomMatch165_7;}}}}}}}if (tomMatch165_11) { tom.engine.adt.tomterm.types.TomTerm  tom_arg= tomMatch165_end_4.getHeadconcTomTerm() ;

        TomSymbol argSymbol = getSymbolFromName(getName(tom_arg));
        // if we have a sublist 
        if(TomBase.isListOperator(argSymbol)) {
          // we can have two cases:
          // 1. the sublist has the codomain = parentListCodomain
          // 2. the sublist has the codomain = expectedType
          if(argSymbol.getTypesToType().getCodomain() == parentListCodomain) {
            validateTerm(tom_arg, parentListCodomain, true, false);            
          } else {
            validateTerm(tom_arg, expectedType, true, false);    
          }        
        } else {
          validateTerm(tom_arg, expectedType, true, false);
        }
      }}if ( tomMatch165_end_4.isEmptyconcTomTerm() ) {tomMatch165_end_4=(( tom.engine.adt.tomterm.types.TomList )args);} else {tomMatch165_end_4= tomMatch165_end_4.getTailconcTomTerm() ;}}} while(!( (tomMatch165_end_4==(( tom.engine.adt.tomterm.types.TomList )args)) ));}}}}

  } //validateListOperatorArgs

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
  } //class TermDescription

  /**
   * verify a BackQuoteAppl term
   * @param subject the backquote term
   * 1. arity
   */
  private void verifyBQAppl(BQTerm subject) throws VisitFailure {
    {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch166_2= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.code.types.BQTerm )subject).getOptions() ;if ( (tomMatch166_2 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch166_2) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch166_2.getString() ;

        int decLine = findOriginTrackingLine(tom_options);
        String fileName = findOriginTrackingFileName(tom_options);
        TomSymbol symbol = getSymbolFromName(tom_name);
        if(symbol==null) {
          // cannot report an error because unknown funtion calls are allowed
          //TomMessage.error(getLogger(), fileName, decLine, TomMessage.unknownSymbol, `name);
        } else {
          //System.out.println("symbol = " + symbol);
          // check the arity (for syntacti operators)
          if(!TomBase.isArrayOperator(symbol) && !TomBase.isListOperator(symbol)) {
            TomTypeList types = symbol.getTypesToType().getDomain();
            int nbExpectedArgs = types.length();
            int nbArgs =  (( tom.engine.adt.code.types.BQTerm )subject).getArgs() .length();
            if(nbArgs != nbExpectedArgs) {
              TomMessage.error(getLogger(), fileName, decLine,
                  TomMessage.symbolNumberArgument,
                  tom_name, nbExpectedArgs, nbArgs);
            }
          }
        }
      }}}}}}}

  }

  private String getName(TomTerm term) {
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch167_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch167_1) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch167_1) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch167_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch167_7= tomMatch167_1.getHeadconcTomName() ;if ( (tomMatch167_7 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch167_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch167_1.getTailconcTomName() .isEmptyconcTomName() ) {
 return  tomMatch167_7.getString() ;}}}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String dijunctionName = tom_nameList.getHeadconcTomName().getString();
        while(!tom_nameList.isEmptyconcTomName()) {
          String head = tom_nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom_nameList= tom_nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch167_14= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch167_14) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch167_14) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch167_14.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch167_20= tomMatch167_14.getHeadconcTomName() ;if ( (tomMatch167_20 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch167_20) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch167_14.getTailconcTomName() .isEmptyconcTomName() ) {
 return  tomMatch167_20.getString() ;}}}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String dijunctionName = tom_nameList.getHeadconcTomName().getString();
        while(!tom_nameList.isEmptyconcTomName()) {
          String head = tom_nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom_nameList= tom_nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch167_27= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch167_27) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch167_27) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch167_27.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch167_34= tomMatch167_27.getHeadconcTomName() ;if ( (tomMatch167_34 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch167_34) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch167_34.getString() ;}}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String dijunctionName = tom_nameList.getHeadconcTomName().getString();
        while(!tom_nameList.isEmptyconcTomName()) {
          String head = tom_nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom_nameList= tom_nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch167_41= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ;if ( (tomMatch167_41 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch167_41) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch167_41.getString() ;}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch167_48= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ;if ( (tomMatch167_48 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch167_48) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch167_48.getString() +"*";}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )term)) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getName( (( tom.engine.adt.tomterm.types.TomTerm )term).getTomTerm() ); }}}}}

    throw new TomRuntimeException("Invalid Term:" + term);
  }

  private String getName(BQTerm term) {
    {{if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )term)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch168_1= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( (tomMatch168_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch168_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch168_1.getString() ;}}}}}}{if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )term)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch168_8= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( (tomMatch168_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch168_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch168_8.getString() ;}}}}}}{if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )term)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch168_15= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( (tomMatch168_15 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch168_15) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch168_15.getString() +"*";}}}}}}{if ( (term instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )term) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )term)) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch168_22= (( tom.engine.adt.code.types.BQTerm )term).getAstName() ;if ( (tomMatch168_22 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch168_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch168_22.getString() ;}}}}}}}

    throw new TomRuntimeException("Invalid Term:" + term);
  }
} //class
