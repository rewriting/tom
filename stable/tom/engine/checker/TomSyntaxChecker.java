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
 * Julien Guyon
 *
 **/

package tom.engine.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.theory.types.*;

import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.SymbolTable;

import tom.library.sl.*;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

        private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_append_list_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList l1,  tom.engine.adt.tomsignature.types.TomSymbolList  l2) {     if( l1.isEmptyconcTomSymbol() ) {       return l2;     } else if( l2.isEmptyconcTomSymbol() ) {       return l1;     } else if(  l1.getTailconcTomSymbol() .isEmptyconcTomSymbol() ) {       return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( l1.getHeadconcTomSymbol() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( l1.getHeadconcTomSymbol() ,tom_append_list_concTomSymbol( l1.getTailconcTomSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.TomSymbolList  tom_get_slice_concTomSymbol( tom.engine.adt.tomsignature.types.TomSymbolList  begin,  tom.engine.adt.tomsignature.types.TomSymbolList  end, tom.engine.adt.tomsignature.types.TomSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomSymbol()  ||  (end== tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make( begin.getHeadconcTomSymbol() ,( tom.engine.adt.tomsignature.types.TomSymbolList )tom_get_slice_concTomSymbol( begin.getTailconcTomSymbol() ,end,tail)) ;   }      private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_append_list_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList l1,  tom.engine.adt.tomdeclaration.types.DeclarationList  l2) {     if( l1.isEmptyconcDeclaration() ) {       return l2;     } else if( l2.isEmptyconcDeclaration() ) {       return l1;     } else if(  l1.getTailconcDeclaration() .isEmptyconcDeclaration() ) {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,l2) ;     } else {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,tom_append_list_concDeclaration( l1.getTailconcDeclaration() ,l2)) ;     }   }   private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_get_slice_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList  begin,  tom.engine.adt.tomdeclaration.types.DeclarationList  end, tom.engine.adt.tomdeclaration.types.DeclarationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcDeclaration()  ||  (end== tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( begin.getHeadconcDeclaration() ,( tom.engine.adt.tomdeclaration.types.DeclarationList )tom_get_slice_concDeclaration( begin.getTailconcDeclaration() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {     if( l1.isEmptyconcPairNameDecl() ) {       return l2;     } else if( l2.isEmptyconcPairNameDecl() ) {       return l1;     } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }     private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ) );}        @SuppressWarnings("unchecked") private static java.util.ArrayList concArrayListAppend(Object o, java.util.ArrayList l) {   java.util.ArrayList res = (java.util.ArrayList)l.clone();   res.add(o);   return res; }    static class MapEntry {   private Object key;   private Object val;   public Object getKey() { return key; }   public Object getVal() { return val; }   public MapEntry(Object key, Object val) {     this.key = key;     this.val = val;   } }      @SuppressWarnings("unchecked") private static java.util.HashMap hashMapAppend(MapEntry e, java.util.HashMap m) {   java.util.HashMap res = (java.util.HashMap) m.clone();   res.put(e.getKey(), e.getVal());   return res; }  @SuppressWarnings("unchecked") private static MapEntry hashMapGetHead(java.util.HashMap m) {   java.util.Set es = m.entrySet();   java.util.Iterator it = es.iterator();   java.util.Map.Entry e = (java.util.Map.Entry) it.next();   return new MapEntry(e.getKey(), e.getValue()); }  @SuppressWarnings("unchecked") private static java.util.HashMap hashMapGetTail(java.util.HashMap m) {   java.util.HashMap res = (java.util.HashMap) m.clone();   java.util.Set es = m.entrySet();   java.util.Iterator it = es.iterator();   java.util.Map.Entry e = (java.util.Map.Entry) it.next();   res.remove(e.getKey());   return res; }   





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
  private  ArrayList<String> alreadyStudiedTypes =  null;
  /** the list of already studied and declared Symbol */
  private  ArrayList<String> alreadyStudiedSymbols =  null;

  /** List of expected functional declaration in each type declaration */
  private final static ArrayList<String> TypeTermSignature =
    new ArrayList<String>(Arrays.asList(new String[]{ TomSyntaxChecker.EQUALS }));

  /** Constructor */
  public TomSyntaxChecker() {
    super("TomSyntaxChecker");
    reinit();
  }

  /**
    * accessors to the inherited attribute "currentTomStructureOrgTrack"
    */
  public Option getCurrentTomStructureOrgTrack() {
    return currentTomStructureOrgTrack;
  }

  public void setCurrentTomStructureOrgTrack(Option currentTomStructureOrgTrack) {
    this.currentTomStructureOrgTrack = currentTomStructureOrgTrack;
  }

  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomSyntaxChecker.DECLARED_OPTIONS);
  }

  protected void reinit() {
    super.reinit();
    alreadyStudiedTypes   = new ArrayList<String>();
    alreadyStudiedSymbols = new ArrayList<String>();
  }

  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom SyntaxChecker : TSM"+getStreamManager().toString());
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        try {
          tom_make_TopDownCollect(tom_make_checkSyntax(this)).visitLight((TomTerm)getWorkingTerm());
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("strategy failed");
        }
        // verbose
        getLogger().log(Level.INFO, TomMessage.tomSyntaxCheckingPhase.getMessage(),
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
                        new Object[]{getClass().getName(),
                                     getStreamManager().getInputFileName(),
                                     e.getMessage() });
        e.printStackTrace();
      }
    } else {
      // syntax checker desactivated
      getLogger().log(Level.INFO, TomMessage.syntaxCheckerInactivated.getMessage());
    }
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noSyntaxCheck");
  }

  /**
   * Syntax checking entry point: Catch and verify all type and operator
   * declaration, Match instruction
   */
  public static class checkSyntax extends tom.library.sl.AbstractStrategyBasic {private  TomSyntaxChecker  tsc;public checkSyntax( TomSyntaxChecker  tsc) {super(( new tom.library.sl.Identity() ));this.tsc=tsc;}public  TomSyntaxChecker  gettsc() {return tsc;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.Strategy) ) { tom.engine.adt.tomsignature.types.TomVisitList  tom_list= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getVisitList() ; tom.engine.adt.tomoption.types.Option  tom_origin= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() ;








        if(tom_list.isEmptyconcTomVisit()) {
          {{if ( (tom_origin instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tom_origin) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 
              tsc.messageError( (( tom.engine.adt.tomoption.types.Option )tom_origin).getFileName() , (( tom.engine.adt.tomoption.types.Option )tom_origin).getLine() ,TomMessage.emptyStrategy,new Object[]{});
            }}}}

          tsc.messageError("unknown",-1,TomMessage.emptyStrategy,new Object[]{});
        }
        /* STRATEGY MATCH STRUCTURE */
        tsc.verifyStrategy(tom_list);
        throw new tom.library.sl.VisitFailure();// stop the top-down
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch81NameNumber_freshVar_5= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch81NameNumber_freshVar_5 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {





        tsc.verifyTypeDecl(TomSyntaxChecker.TYPE_TERM,  tomMatch81NameNumber_freshVar_5.getString() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getDeclarations() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() );
        throw new tom.library.sl.VisitFailure();// stop the top-down
      }}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch81NameNumber_freshVar_12= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch81NameNumber_freshVar_12 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        tsc.verifySymbol(TomSyntaxChecker.CONSTRUCTOR, tsc.getSymbolFromName( tomMatch81NameNumber_freshVar_12.getString() ));
        throw new tom.library.sl.VisitFailure();// stop the top-down
      }}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch81NameNumber_freshVar_17= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch81NameNumber_freshVar_17 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        tsc.verifySymbol(TomSyntaxChecker.OP_ARRAY, tsc.getSymbolFromName( tomMatch81NameNumber_freshVar_17.getString() ));
        throw new tom.library.sl.VisitFailure();// stop the top-down
      }}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch81NameNumber_freshVar_22= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;if ( (tomMatch81NameNumber_freshVar_22 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        tsc.verifySymbol(TomSyntaxChecker.OP_LIST, tsc.getSymbolFromName( tomMatch81NameNumber_freshVar_22.getString() ));
        throw new tom.library.sl.VisitFailure();// stop the top-down
      }}}}}return _visit_Declaration(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {




        /* TOM MATCH STRUCTURE */
        tsc.verifyMatch( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() ,  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOption() );
      }}}}return _visit_Instruction(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BackQuoteAppl) ) {




        tsc.verifyBackQuoteAppl((( tom.engine.adt.tomterm.types.TomTerm )tom__arg));
      }}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_checkSyntax( TomSyntaxChecker  t0) { return new checkSyntax(t0);}



  // /////////////////////////////
  // TYPE DECLARATION CONCERNS //
  // ////////////////////////////
  private void verifyTypeDecl(String declType, String tomName, DeclarationList listOfDeclaration, Option typeOrgTrack) {
    setCurrentTomStructureOrgTrack(typeOrgTrack);
    // ensure first definition
    verifyMultipleDefinition(tomName, declType, TYPE);
    // verify Macro functions
    ArrayList<String> verifyList = new ArrayList<String>(TomSyntaxChecker.TypeTermSignature);

    {{if ( (listOfDeclaration instanceof tom.engine.adt.tomdeclaration.types.DeclarationList) ) {if ( (((( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration) || ((( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration)) ) { tom.engine.adt.tomdeclaration.types.DeclarationList  tomMatch85NameNumber_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration);do {{if (!( tomMatch85NameNumber_end_4.isEmptyconcDeclaration() )) {
 // for each Declaration
        Declaration decl =  tomMatch85NameNumber_end_4.getHeadconcDeclaration() ;
        matchblock:{
          {{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch86NameNumber_freshVar_1= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getTermArg1() ; tom.engine.adt.tomterm.types.TomTerm  tomMatch86NameNumber_freshVar_2= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getTermArg2() ;if ( (tomMatch86NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch86NameNumber_freshVar_6= tomMatch86NameNumber_freshVar_1.getAstName() ;if ( (tomMatch86NameNumber_freshVar_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch86NameNumber_freshVar_2 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch86NameNumber_freshVar_10= tomMatch86NameNumber_freshVar_2.getAstName() ;if ( (tomMatch86NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(TomSyntaxChecker.EQUALS,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() , tomMatch86NameNumber_freshVar_6.getString() , tomMatch86NameNumber_freshVar_10.getString() ,declType)


;
              break matchblock;
            }}}}}}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {checkField(TomSyntaxChecker.GET_HEAD,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() ,declType)


;
              break matchblock;
            }}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {checkField(TomSyntaxChecker.GET_TAIL,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() ,declType)

;
              break matchblock;
            }}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {checkField(TomSyntaxChecker.IS_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() ,declType)

;
              break matchblock;
            }}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch86NameNumber_freshVar_24= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getVariable() ; tom.engine.adt.tomterm.types.TomTerm  tomMatch86NameNumber_freshVar_25= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getIndex() ;if ( (tomMatch86NameNumber_freshVar_24 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch86NameNumber_freshVar_28= tomMatch86NameNumber_freshVar_24.getAstName() ;if ( (tomMatch86NameNumber_freshVar_28 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch86NameNumber_freshVar_25 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch86NameNumber_freshVar_32= tomMatch86NameNumber_freshVar_25.getAstName() ;if ( (tomMatch86NameNumber_freshVar_32 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(TomSyntaxChecker.GET_ELEMENT,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() , tomMatch86NameNumber_freshVar_28.getString() , tomMatch86NameNumber_freshVar_32.getString() ,declType)


;
              break matchblock;
            }}}}}}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {checkField(TomSyntaxChecker.GET_SIZE,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() ,declType)

;
              break matchblock;
            }}}}

                   }
      }if ( tomMatch85NameNumber_end_4.isEmptyconcDeclaration() ) {tomMatch85NameNumber_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration);} else {tomMatch85NameNumber_end_4= tomMatch85NameNumber_end_4.getTailconcDeclaration() ;}}} while(!( (tomMatch85NameNumber_end_4==(( tom.engine.adt.tomdeclaration.types.DeclarationList )listOfDeclaration)) ));}}}}

    // remove non mandatory functions
    if(verifyList.contains(TomSyntaxChecker.EQUALS)) {
      verifyList.remove(verifyList.indexOf(TomSyntaxChecker.EQUALS));
    }
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(declType, verifyList);
    }
  } // verifyTypeDecl

  private void verifyMultipleDefinition(String name, String symbolType, String OperatorOrType) {
    ArrayList list;
    if(OperatorOrType.equals(TomSyntaxChecker.OPERATOR)) {
      if(alreadyStudiedSymbols.contains(name)) {
        messageError(getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.multipleSymbolDefinitionError,
            new Object[]{name});
      } else {
        alreadyStudiedSymbols.add(name);
      }
    } else {
      if(alreadyStudiedTypes.contains(name)) {
        messageWarning(getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.multipleSortDefinitionError,
            new Object[]{name});
      } else {
        alreadyStudiedTypes.add(name);
      }
    }
  } // verifyMultipleDefinition

  private void checkField(String function, ArrayList foundFunctions, Option orgTrack, String symbolType) {
    if(foundFunctions.contains(function)) {
      foundFunctions.remove(foundFunctions.indexOf(function));
    } else {
      messageError(orgTrack.getFileName(),orgTrack.getLine(),
                   TomMessage.macroFunctionRepeated,
                   new Object[]{function});
    }
  } // checkField

  private  void checkFieldAndLinearArgs(String function, ArrayList foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      messageError(orgTrack.getFileName(),orgTrack.getLine(),
                   TomMessage.nonLinearMacroFunction,
                   new Object[]{function, name1});
    }
  } // checkFieldAndLinearArgs

  // ///////////////////////////////
  // SYMBOL DECLARATION CONCERNS //
  // ///////////////////////////////
  private void verifySymbol(String symbolType, TomSymbol tomSymbol){
    int domainLength;
    String symbStrName = tomSymbol.getAstName().getString();
    OptionList optionList = tomSymbol.getOption();
    // We save first the origin tracking of the symbol declaration
    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(optionList));

    // ensure first definition then Codomain, Domain, Macros and Slots (Simple
    // operator)
    verifyMultipleDefinition(symbStrName, symbolType, TomSyntaxChecker.OPERATOR);
    verifySymbolCodomain(TomBase.getSymbolCodomain(tomSymbol), symbStrName, symbolType);
    domainLength = verifySymbolDomain(TomBase.getSymbolDomain(tomSymbol), symbStrName, symbolType);
    verifySymbolMacroFunctions(optionList, domainLength, symbolType);
  } // verifySymbol

  private void verifySymbolCodomain(TomType codomain, String symbName, String symbolType) {
    {{if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.tomtype.Codomain) ) { tom.engine.adt.tomname.types.TomName  tomMatch87NameNumber_freshVar_1= (( tom.engine.adt.tomtype.types.TomType )codomain).getAstName() ;if ( (tomMatch87NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        if(symbolTable().getSymbolFromName( tomMatch87NameNumber_freshVar_1.getString() ) == null) {
          messageError(getCurrentTomStructureOrgTrack().getFileName(),getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              new Object[]{symbName, codomain});
        }
        return;
      }}}}{if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone) ) { String  tom_typeName= (( tom.engine.adt.tomtype.types.TomType )codomain).getString() ;


        if(!testTypeExistence(tom_typeName)) {
          messageError(getCurrentTomStructureOrgTrack().getFileName(),getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              new Object[]{symbName, (tom_typeName)});
          if(tom_typeName.equals("Strategy")) {
          messageError(getCurrentTomStructureOrgTrack().getFileName(),getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.missingIncludeSL,
              new Object[]{});

          }
        }
        return;
      }}}{if ( (codomain instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )codomain) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {


        messageError(getCurrentTomStructureOrgTrack().getFileName(),getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.symbolCodomainError,
            new Object[]{symbName, ""});
        return;
      }}}}

    throw new TomRuntimeException("Strange codomain "+codomain);
  }

  private int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
    int position = 1;
    if(symbolType.equals(TomSyntaxChecker.CONSTRUCTOR)) {
      {{if ( (args instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch88NameNumber_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )args);do {{if (!( tomMatch88NameNumber_end_4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch88NameNumber_freshVar_8= tomMatch88NameNumber_end_4.getHeadconcTomType() ;if ( (tomMatch88NameNumber_freshVar_8 instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone) ) { String  tom_typeName= tomMatch88NameNumber_freshVar_8.getString() ;
 // for each symbol types
          if(!testTypeExistence(tom_typeName)) {
            messageError(getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.symbolDomainError,
                new Object[]{Integer.valueOf(position), symbName, (tom_typeName)});
          }
          position++;
        }}if ( tomMatch88NameNumber_end_4.isEmptyconcTomType() ) {tomMatch88NameNumber_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )args);} else {tomMatch88NameNumber_end_4= tomMatch88NameNumber_end_4.getTailconcTomType() ;}}} while(!( (tomMatch88NameNumber_end_4==(( tom.engine.adt.tomtype.types.TomTypeList )args)) ));}}}}

      return (position-1);
    } else { // OPARRAY and OPLIST
      {{if ( (args instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )args) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( (( tom.engine.adt.tomtype.types.TomTypeList )args).isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch89NameNumber_freshVar_4= (( tom.engine.adt.tomtype.types.TomTypeList )args).getHeadconcTomType() ;if ( (tomMatch89NameNumber_freshVar_4 instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone) ) { String  tom_typeName= tomMatch89NameNumber_freshVar_4.getString() ;if (  (( tom.engine.adt.tomtype.types.TomTypeList )args).getTailconcTomType() .isEmptyconcTomType() ) {

          if(!testTypeExistence(tom_typeName)) {
            messageError(getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.listSymbolDomainError,
                new Object[]{symbName, (tom_typeName)});
          }
        }}}}}}}
 // match
      return 1;
    }
  } // verifySymbolDomain

  private void verifySymbolMacroFunctions(OptionList option, int domainLength, String symbolType) {
    ArrayList<String> verifyList = new ArrayList<String>();
    boolean foundOpMake = false;
    if(symbolType.equals(TomSyntaxChecker.CONSTRUCTOR)) { // Nothing absolutely
                                                          // necessary
    } else if(symbolType == TomSyntaxChecker.OP_ARRAY ) {
      verifyList.add(TomSyntaxChecker.MAKE_EMPTY);
      verifyList.add(TomSyntaxChecker.MAKE_APPEND);
    } else if(symbolType == TomSyntaxChecker.OP_LIST) {
      verifyList.add(TomSyntaxChecker.MAKE_EMPTY);
      verifyList.add(TomSyntaxChecker.MAKE_INSERT);
    }

    {{if ( (option instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )option) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )option) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch90NameNumber_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);do {{if (!( tomMatch90NameNumber_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch90NameNumber_freshVar_8= tomMatch90NameNumber_end_4.getHeadconcOption() ;if ( (tomMatch90NameNumber_freshVar_8 instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {
 // for each Declaration
        Declaration decl= tomMatch90NameNumber_freshVar_8.getAstDeclaration() ;
        matchblock:{
          {{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {checkField(TomSyntaxChecker.MAKE_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() ,symbolType)


;
              break matchblock;
            }}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch91NameNumber_freshVar_4= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getVarList() ; tom.engine.adt.tomterm.types.TomTerm  tomMatch91NameNumber_freshVar_5= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getVarElt() ;if ( (tomMatch91NameNumber_freshVar_4 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch91NameNumber_freshVar_8= tomMatch91NameNumber_freshVar_4.getAstName() ;if ( (tomMatch91NameNumber_freshVar_8 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch91NameNumber_freshVar_5 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch91NameNumber_freshVar_12= tomMatch91NameNumber_freshVar_5.getAstName() ;if ( (tomMatch91NameNumber_freshVar_12 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(TomSyntaxChecker.MAKE_APPEND,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() , tomMatch91NameNumber_freshVar_8.getString() , tomMatch91NameNumber_freshVar_12.getString() ,symbolType)

;
              break matchblock;
            }}}}}}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {checkField(TomSyntaxChecker.MAKE_EMPTY,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() ,symbolType)


;
              break matchblock;
            }}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch91NameNumber_freshVar_20= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getVarList() ; tom.engine.adt.tomterm.types.TomTerm  tomMatch91NameNumber_freshVar_21= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getVarElt() ;if ( (tomMatch91NameNumber_freshVar_20 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch91NameNumber_freshVar_24= tomMatch91NameNumber_freshVar_20.getAstName() ;if ( (tomMatch91NameNumber_freshVar_24 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch91NameNumber_freshVar_21 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch91NameNumber_freshVar_28= tomMatch91NameNumber_freshVar_21.getAstName() ;if ( (tomMatch91NameNumber_freshVar_28 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {checkFieldAndLinearArgs(TomSyntaxChecker.MAKE_INSERT,verifyList, (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() , tomMatch91NameNumber_freshVar_24.getString() , tomMatch91NameNumber_freshVar_28.getString() ,symbolType)

;
              break matchblock;
            }}}}}}}{if ( (decl instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )decl) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomoption.types.Option  tomMatch91NameNumber_freshVar_34= (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getOrgTrack() ;if ( (tomMatch91NameNumber_freshVar_34 instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {


              if (!foundOpMake) {
                foundOpMake = true;
                verifyMakeDeclArgs( (( tom.engine.adt.tomdeclaration.types.Declaration )decl).getArgs() ,domainLength,tomMatch91NameNumber_freshVar_34,symbolType);
              } else {
                messageError( tomMatch91NameNumber_freshVar_34.getFileName() ,  tomMatch91NameNumber_freshVar_34.getLine() ,
                             TomMessage.macroFunctionRepeated,
                             new Object[]{TomSyntaxChecker.MAKE});
              }
              break matchblock;
            }}}}}

        }
      }}if ( tomMatch90NameNumber_end_4.isEmptyconcOption() ) {tomMatch90NameNumber_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);} else {tomMatch90NameNumber_end_4= tomMatch90NameNumber_end_4.getTailconcOption() ;}}} while(!( (tomMatch90NameNumber_end_4==(( tom.engine.adt.tomoption.types.OptionList )option)) ));}}}}

    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(symbolType, verifyList);
    }
  }  // verifySymbolMacroFunctions

  private void verifyMakeDeclArgs(TomList argsList, int domainLength, Option orgTrack, String symbolType){
      // we test the necessity to use different names for each
      // variable-parameter.
    int nbArgs = 0;
    ArrayList<String> listVar = new ArrayList<String>();
    {{if ( (argsList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )argsList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )argsList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch92NameNumber_end_4=(( tom.engine.adt.tomterm.types.TomList )argsList);do {{if (!( tomMatch92NameNumber_end_4.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch92NameNumber_freshVar_8= tomMatch92NameNumber_end_4.getHeadconcTomTerm() ;if ( (tomMatch92NameNumber_freshVar_8 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch92NameNumber_freshVar_7= tomMatch92NameNumber_freshVar_8.getAstName() ;if ( (tomMatch92NameNumber_freshVar_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch92NameNumber_freshVar_7.getString() ;
 // for each Macro variable
        if(listVar.contains(tom_name)) {
          messageError(orgTrack.getFileName(),orgTrack.getLine(),
                       TomMessage.nonLinearMacroFunction,
                       new Object[]{TomSyntaxChecker.MAKE, (tom_name)});
        } else {
          listVar.add(tom_name);
        }
        nbArgs++;
      }}}if ( tomMatch92NameNumber_end_4.isEmptyconcTomTerm() ) {tomMatch92NameNumber_end_4=(( tom.engine.adt.tomterm.types.TomList )argsList);} else {tomMatch92NameNumber_end_4= tomMatch92NameNumber_end_4.getTailconcTomTerm() ;}}} while(!( (tomMatch92NameNumber_end_4==(( tom.engine.adt.tomterm.types.TomList )argsList)) ));}}}}

    if(nbArgs != domainLength) {
      messageError(orgTrack.getFileName(),orgTrack.getLine(),
                   TomMessage.badMakeDefinition,
                   new Object[]{Integer.valueOf(nbArgs), Integer.valueOf(domainLength)});
    }
  } // verifyMakeDeclArgs

  private void verifySymbolPairNameDeclList(PairNameDeclList pairNameDeclList, String symbolType) {
      // we test the existence of 2 same slot names
    ArrayList<String> listSlot = new ArrayList<String>();
    {{if ( (pairNameDeclList instanceof tom.engine.adt.tomslot.types.PairNameDeclList) ) {if ( (((( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl) || ((( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl)) ) { tom.engine.adt.tomslot.types.PairNameDeclList  tomMatch93NameNumber_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList);do {{if (!( tomMatch93NameNumber_end_4.isEmptyconcPairNameDecl() )) { tom.engine.adt.tomslot.types.PairNameDecl  tomMatch93NameNumber_freshVar_8= tomMatch93NameNumber_end_4.getHeadconcPairNameDecl() ;if ( (tomMatch93NameNumber_freshVar_8 instanceof tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch93NameNumber_freshVar_7= tomMatch93NameNumber_freshVar_8.getSlotName() ;if ( (tomMatch93NameNumber_freshVar_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch93NameNumber_freshVar_7.getString() ;
 // for each Slot
        if(listSlot.contains(tom_name)) {
            // TODO
            // messageWarningTwoSameSlotDeclError(name, orgTrack, symbolType);
        } else {
          listSlot.add(tom_name);
        }
      }}}if ( tomMatch93NameNumber_end_4.isEmptyconcPairNameDecl() ) {tomMatch93NameNumber_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList);} else {tomMatch93NameNumber_end_4= tomMatch93NameNumber_end_4.getTailconcPairNameDecl() ;}}} while(!( (tomMatch93NameNumber_end_4==(( tom.engine.adt.tomslot.types.PairNameDeclList )pairNameDeclList)) ));}}}}

  } // verifySymbolPairNameDeclList

  private void messageMissingMacroFunctions(String symbolType, ArrayList list) {
    StringBuilder listOfMissingMacros = new StringBuilder();
    for(int i=0;i<list.size();i++) {
      listOfMissingMacros.append(list.get(i) + ",  ");
    }
    String stringListOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
    messageError(getCurrentTomStructureOrgTrack().getFileName(),
        getCurrentTomStructureOrgTrack().getLine(),
                 TomMessage.missingMacroFunctions,
                 new Object[]{stringListOfMissingMacros});
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
  private void verifyMatch(ConstraintInstructionList constraintInstructionList, OptionList option) throws VisitFailure {
    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(option));
    ArrayList<Constraint> constraints = new ArrayList<Constraint>();    
    HashMap<TomName, List<TomName>> varRelationsMap = new HashMap<TomName, List<TomName>>();
    HashMap<Constraint, Instruction> orActionMap = new HashMap<Constraint, Instruction>();
    ArrayList tmp = new ArrayList();
    tom_make_TopDownCollect(tom_make_CollectConstraints(constraints,tmp,orActionMap)).visitLight(constraintInstructionList);
    TomType typeMatch = null;    
    for(Constraint constr: constraints) {
matchLbl: {{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() ; tom.engine.adt.tomterm.types.TomTerm  tom_subject= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ;
          
          ArrayList<TomName> patternVars = new ArrayList<TomName>();
          ArrayList<TomName> subjectVars = new ArrayList<TomName>();
          tom_make_TopDownCollect(tom_make_CollectVariables(patternVars)).visitLight(tom_pattern);
          tom_make_TopDownCollect(tom_make_CollectVariables(subjectVars)).visitLight(tom_subject);
          
          computeDependencies(varRelationsMap,patternVars,subjectVars);
          {{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.TomTypeToTomTerm) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstType()  instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone) ) {

              // this is from %strategy construct and is already checked in verifyStrategy              
              break matchLbl;
            }}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {

              typeMatch = getSubjectType(tom_subject,constraints);
            }}}

          if(typeMatch == null) {
            Object messageContent = tom_subject;
            {{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch96NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ;if ( (tomMatch96NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

                messageContent =  tomMatch96NameNumber_freshVar_1.getString() ;
              }}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch96NameNumber_freshVar_6= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getNameList() ;if ( ((tomMatch96NameNumber_freshVar_6 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch96NameNumber_freshVar_6 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch96NameNumber_freshVar_6.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch96NameNumber_freshVar_12= tomMatch96NameNumber_freshVar_6.getHeadconcTomName() ;if ( (tomMatch96NameNumber_freshVar_12 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

                messageContent =  tomMatch96NameNumber_freshVar_12.getString() ;
              }}}}}}}

            messageError(getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.cannotGuessMatchType,
                new Object[]{(messageContent)});

            return;
          }

          // we now compare the pattern to its definition
          verifyMatchPattern(tom_pattern, typeMatch);
        }}}{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() ; tom.engine.adt.tomterm.types.TomTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ;








          // the lhs and rhs can only be TermAppl or Variable
          {{if ( (tom_left instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch97NameNumber_freshVar_2= false ;boolean tomMatch97NameNumber_freshVar_3= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_left) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch97NameNumber_freshVar_3= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_left) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {tomMatch97NameNumber_freshVar_3= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_left) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) {tomMatch97NameNumber_freshVar_3= true ;}}}if ((tomMatch97NameNumber_freshVar_3 ==  true )) {tomMatch97NameNumber_freshVar_2= true ;}if ((tomMatch97NameNumber_freshVar_2 ==  false )) {
              
              messageError(getCurrentTomStructureOrgTrack().getFileName(),
                  getCurrentTomStructureOrgTrack().getLine(),
                  TomMessage.termOrVariableNumericLeft,
                  new Object[]{getName((tom_left))});
              return;
            }}}}
        
          // the rhs can only be TermAppl or Variable
          {{if ( (tom_right instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch98NameNumber_freshVar_2= false ;boolean tomMatch98NameNumber_freshVar_3= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_right) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch98NameNumber_freshVar_3= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_right) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {tomMatch98NameNumber_freshVar_3= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_right) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) {tomMatch98NameNumber_freshVar_3= true ;}}}if ((tomMatch98NameNumber_freshVar_3 ==  true )) {tomMatch98NameNumber_freshVar_2= true ;}if ((tomMatch98NameNumber_freshVar_2 ==  false )) {

              messageError(getCurrentTomStructureOrgTrack().getFileName(),
                  getCurrentTomStructureOrgTrack().getLine(),
                  TomMessage.termOrVariableNumericRight,
                  new Object[]{getName((tom_right))});
              return;
            }}}}tom_make_TopDown(tom_make_CheckNumeric(tom_left,this))

.visitLight(tom_left);
          tom_make_TopDown(tom_make_CheckNumeric(tom_right,this)).visitLight(tom_right);
          // if we have the type, check that it is the same
          TomType leftType = TomBase.getTermType(tom_left,symbolTable());
          TomType rightType = TomBase.getTermType(tom_right,symbolTable());
          // if the types are not available, leave the error to be raised by java
          if(leftType != null && leftType != SymbolTable.TYPE_UNKNOWN && leftType !=  tom.engine.adt.tomtype.types.tomtype.EmptyType.make()  
             && rightType != null && rightType != SymbolTable.TYPE_UNKNOWN && rightType !=  tom.engine.adt.tomtype.types.tomtype.EmptyType.make()  
             && (leftType != rightType)) {
            messageError(getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.invalidTypesNumeric,
                new Object[]{(leftType),getName((tom_left)),(rightType),getName((tom_right))}); 
            return;
          }
        }}}{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_oc=(( tom.engine.adt.tomconstraint.types.Constraint )constr);


          if(!verifyOrConstraint(tom_oc,orActionMap.get(tom_oc))) {
            return;
          }
        }}}}

    } // for
    
    checkVarDependencies(varRelationsMap);
  }
  
  /**
   * Puts all the variables in the list patternVars in relation with all the variables in subjectVars
   */
  private void computeDependencies(HashMap<TomName, List<TomName>>  varRelationsMap, List<TomName> patternVars, List<TomName> subjectVars){      
    for(TomName x:patternVars) {      
      if(!varRelationsMap.keySet().contains(x)) {
        varRelationsMap.put(x,subjectVars);
      } else { // add the rest of the variables
        List<TomName> tmp = new ArrayList<TomName>(subjectVars);
        tmp.addAll(varRelationsMap.get(x));
        varRelationsMap.put(x,subjectVars);
      }
    }
  }
  
  
  /**
   * Checks that there is not a circular reference of a variable
   */
  private void checkVarDependencies(HashMap<TomName, List<TomName>> varRelationsMap) {
    for (TomName var:varRelationsMap.keySet()) {
      isVariablePresent(var, varRelationsMap.get(var), varRelationsMap, new ArrayList<TomName>());
    }
  }
  
  private void isVariablePresent(TomName var, List<TomName> associatedList, HashMap<TomName, List<TomName>> varRelationsMap, List<TomName> checked) {    
    if(associatedList.contains(var)) {
      messageError(getCurrentTomStructureOrgTrack().getFileName(),
          getCurrentTomStructureOrgTrack().getLine(),
          TomMessage.circularReferences,
          new Object[]{((Name)var).getString()});       
    } else {
      for(TomName tn: associatedList) {
        if(checked.contains(tn)) { return; }
        checked.add(tn);
        List<TomName> lst = varRelationsMap.get(tn);
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
    ArrayList<TomTerm> freeVarList1 = new ArrayList<TomTerm>();
    ArrayList<TomTerm> freeVarList2 = new ArrayList<TomTerm>();
    {{if ( (orConstraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )orConstraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )orConstraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch99NameNumber_end_4=(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint);do {{if (!( (  tomMatch99NameNumber_end_4.isEmptyOrConstraint()  ||  (tomMatch99NameNumber_end_4== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) { tom.engine.adt.tomconstraint.types.Constraint  tom_x=(( ((tomMatch99NameNumber_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (tomMatch99NameNumber_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( tomMatch99NameNumber_end_4.getHeadOrConstraint() ):(tomMatch99NameNumber_end_4));

        // we collect the free vars only from match constraints
        // and we check these variables for numeric constraints also
        // ex: 'y << a() || x > 3' should generate an error 
        {{if ( (tom_x instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom_x) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {tom_make_TopDownCollect(tom_make_CollectFreeVar(freeVarList2))

.visitLight( (( tom.engine.adt.tomconstraint.types.Constraint )tom_x).getPattern() );
          }}}{if ( (tom_x instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )tom_x) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )tom_x) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch100NameNumber_end_8=(( tom.engine.adt.tomconstraint.types.Constraint )tom_x);do {{if (!( (  tomMatch100NameNumber_end_8.isEmptyAndConstraint()  ||  (tomMatch100NameNumber_end_8== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch100NameNumber_freshVar_13=(( ((tomMatch100NameNumber_end_8 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch100NameNumber_end_8 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch100NameNumber_end_8.getHeadAndConstraint() ):(tomMatch100NameNumber_end_8));if ( (tomMatch100NameNumber_freshVar_13 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {tom_make_TopDownCollect(tom_make_CollectFreeVar(freeVarList2))

.visitLight( tomMatch100NameNumber_freshVar_13.getPattern() );
          }}if ( (  tomMatch100NameNumber_end_8.isEmptyAndConstraint()  ||  (tomMatch100NameNumber_end_8== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {tomMatch100NameNumber_end_8=(( tom.engine.adt.tomconstraint.types.Constraint )tom_x);} else {tomMatch100NameNumber_end_8=(( ((tomMatch100NameNumber_end_8 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch100NameNumber_end_8 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch100NameNumber_end_8.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));}}} while(!( (tomMatch100NameNumber_end_8==(( tom.engine.adt.tomconstraint.types.Constraint )tom_x)) ));}}}}
        
        if(!freeVarList1.isEmpty()) {
          for(TomTerm term:freeVarList2) {
            if (!freeVarList1.contains(term)) {
              if(containsVariable(term, action)) {              
                String varName = (term instanceof Variable) ? ((Variable)term).getAstName().getString() : ((VariableStar)term).getAstName().getString();  
                messageError(getCurrentTomStructureOrgTrack().getFileName(),
                    getCurrentTomStructureOrgTrack().getLine(),
                    TomMessage.freeVarNotPresentInOr,
                    new Object[]{varName});
                return false;
              }
            }
          }
          for(TomTerm term:freeVarList1) {
            if (!freeVarList2.contains(term)) {
              if(containsVariable(term, action))  {
                String varName = (term instanceof Variable) ? ((Variable)term).getAstName().getString() : ((VariableStar)term).getAstName().getString();  
                messageError(getCurrentTomStructureOrgTrack().getFileName(),
                    getCurrentTomStructureOrgTrack().getLine(),
                    TomMessage.freeVarNotPresentInOr,
                    new Object[]{varName});
                return false;
              }
            }
          }          
        }
        freeVarList1 = new ArrayList<TomTerm>(freeVarList2);
        freeVarList2.clear();
      }if ( (  tomMatch99NameNumber_end_4.isEmptyOrConstraint()  ||  (tomMatch99NameNumber_end_4== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) ) {tomMatch99NameNumber_end_4=(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint);} else {tomMatch99NameNumber_end_4=(( ((tomMatch99NameNumber_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (tomMatch99NameNumber_end_4 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( tomMatch99NameNumber_end_4.getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ));}}} while(!( (tomMatch99NameNumber_end_4==(( tom.engine.adt.tomconstraint.types.Constraint )orConstraint)) ));}}}}

    return true;
  }
  
  private boolean containsVariable(TomTerm var, Instruction action) {
    try {
      tom_make_TopDown(tom_make_ContainsVariable(var)).visitLight(action);
    } catch(VisitFailure e) {
      return true;
    }
    return false;
  }
  public static class ContainsVariable extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomterm.types.TomTerm  var;public ContainsVariable( tom.engine.adt.tomterm.types.TomTerm  var) {super(( new tom.library.sl.Identity() ));this.var=var;}public  tom.engine.adt.tomterm.types.TomTerm  getvar() {return var;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch101NameNumber_freshVar_2= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch101NameNumber_freshVar_2= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch101NameNumber_freshVar_2= true ;}}if ((tomMatch101NameNumber_freshVar_2 ==  true )) {


        TomTerm newvar = (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).setOption( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ); // to avoid problems related to line numbers
        if(newvar==var) {
          throw new VisitFailure();
        }
      }}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_ContainsVariable( tom.engine.adt.tomterm.types.TomTerm  t0) { return new ContainsVariable(t0);}


  
  /**
   * Collect the free variables in an constraint (do not inspect under a anti)  
   */
  public static class CollectFreeVar extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectFreeVar( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch102NameNumber_freshVar_2= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch102NameNumber_freshVar_2= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch102NameNumber_freshVar_2= true ;}}if ((tomMatch102NameNumber_freshVar_2 ==  true )) { tom.engine.adt.tomterm.types.TomTerm  tom_v=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);


        TomTerm newvar = tom_v.setOption( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ); // to avoid problems related to line numbers
        if(!varList.contains(newvar)) { 
          varList.add(tom_v); 
        }
        throw new VisitFailure();// to stop the top-down
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
        
        throw new VisitFailure();// to stop the top-down
      }}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectFreeVar( java.util.Collection  t0) { return new CollectFreeVar(t0);}


  
  /**
   * Check numeric constraint 
   *     1. no annotations
   *     2. no annonymous vars
   *     3. no anti-patterns
   *     4. implicit notation forbidden  
   */
  public static class CheckNumeric extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomterm.types.TomTerm  toCheck;private  TomSyntaxChecker  tsc;public CheckNumeric( tom.engine.adt.tomterm.types.TomTerm  toCheck,  TomSyntaxChecker  tsc) {super(( new tom.library.sl.Identity() ));this.toCheck=toCheck;this.tsc=tsc;}public  tom.engine.adt.tomterm.types.TomTerm  gettoCheck() {return toCheck;}public  TomSyntaxChecker  gettsc() {return tsc;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {


        tsc.raiseError(toCheck,TomMessage.forbiddenImplicitNumeric);        
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {

        tsc.raiseError(toCheck,TomMessage.forbiddenAntiTermInNumeric);        
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch103NameNumber_freshVar_6= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {tomMatch103NameNumber_freshVar_6= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {tomMatch103NameNumber_freshVar_6= true ;}}if ((tomMatch103NameNumber_freshVar_6 ==  true )) {

        tsc.raiseError(toCheck,TomMessage.forbiddenAnonymousInNumeric);        
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch103NameNumber_freshVar_12= false ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch103NameNumber_freshVar_8= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch103NameNumber_freshVar_12= true ;tomMatch103NameNumber_freshVar_8= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch103NameNumber_freshVar_12= true ;tomMatch103NameNumber_freshVar_8= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;}}}if ((tomMatch103NameNumber_freshVar_12 ==  true )) {boolean tomMatch103NameNumber_freshVar_11= false ;if ( ((tomMatch103NameNumber_freshVar_8 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch103NameNumber_freshVar_8 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if ( tomMatch103NameNumber_freshVar_8.isEmptyconcConstraint() ) {tomMatch103NameNumber_freshVar_11= true ;}}if ((tomMatch103NameNumber_freshVar_11 ==  false )) {
  
        tsc.raiseError(toCheck,TomMessage.forbiddenAnnotationsNumeric);        
      }}}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CheckNumeric( tom.engine.adt.tomterm.types.TomTerm  t0,  TomSyntaxChecker  t1) { return new CheckNumeric(t0,t1);}


  private void raiseError(TomTerm arg, TomMessage msg){    
    /** messageError("tom.engine.checker.TomSyntaxChecker",currentTomStructureOrgTrack.getFileName(), */
    messageError("tom.engine.checker.TomSyntaxChecker",getCurrentTomStructureOrgTrack().getFileName(),
        getCurrentTomStructureOrgTrack().getLine(),
        msg,new Object[]{getName(arg)});
  }  
  
  /**
   * Collect the variables' names   
   */
  public static class CollectVariables extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varList;public CollectVariables( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch104NameNumber_freshVar_3= false ; tom.engine.adt.tomname.types.TomName  tomMatch104NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch104NameNumber_freshVar_3= true ;tomMatch104NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch104NameNumber_freshVar_3= true ;tomMatch104NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}}}if ((tomMatch104NameNumber_freshVar_3 ==  true )) { tom.engine.adt.tomname.types.TomName  tom_name=tomMatch104NameNumber_freshVar_1;

        
        if(!varList.contains(tom_name)) {
          varList.add(tom_name); 
        }
        throw new VisitFailure();// to stop the top-down
      }}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectVariables( java.util.Collection  t0) { return new CollectVariables(t0);}


  
  /**
   * tries to give the type of the tomTerm received as parameter
   */
  private TomType getSubjectType(TomTerm subject, ArrayList<Constraint> constraints) {
    {{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch105NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch105NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstType() ;if ( (tomMatch105NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch105NameNumber_freshVar_2 instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone) ) { String  tom_type= tomMatch105NameNumber_freshVar_2.getString() ; tom.engine.adt.tomtype.types.TomType  tom_tomType=tomMatch105NameNumber_freshVar_2;
        
        if(tom_tomType==SymbolTable.TYPE_UNKNOWN) {
          // try to guess
          return guessSubjectType(subject,constraints);
        } else if(testTypeExistence(tom_type)) {
          return tom_tomType;
        } else {
          messageError(getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.unknownMatchArgumentTypeInSignature,
              new Object[]{ tomMatch105NameNumber_freshVar_1.getString() , (tom_type)});                
        }
      }}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch105NameNumber_freshVar_9= (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ;if ( ((tomMatch105NameNumber_freshVar_9 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch105NameNumber_freshVar_9 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch105NameNumber_freshVar_9.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch105NameNumber_freshVar_14= tomMatch105NameNumber_freshVar_9.getHeadconcTomName() ;if ( (tomMatch105NameNumber_freshVar_14 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch105NameNumber_freshVar_14.getString() ;if (  tomMatch105NameNumber_freshVar_9.getTailconcTomName() .isEmptyconcTomName() ) {

        TomSymbol symbol = getSymbolFromName(tom_name);
        if(symbol!=null) {
          TomType type = TomBase.getSymbolCodomain(symbol);
          String typeName = TomBase.getTomType(type);
          if(!testTypeExistence(typeName)) {
            messageError(getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                new Object[]{tom_name, typeName});
          }          
          validateTerm((( tom.engine.adt.tomterm.types.TomTerm )subject), type, false, true, true);
          return type;
        } else {
          // try to guess
          return guessSubjectType(subject,constraints);
        }
      }}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch105NameNumber_freshVar_16= (( tom.engine.adt.tomterm.types.TomTerm )subject).getTomTerm() ;if ( (tomMatch105NameNumber_freshVar_16 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch105NameNumber_freshVar_19= tomMatch105NameNumber_freshVar_16.getNameList() ;if ( ((tomMatch105NameNumber_freshVar_19 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch105NameNumber_freshVar_19 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch105NameNumber_freshVar_19.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch105NameNumber_freshVar_24= tomMatch105NameNumber_freshVar_19.getHeadconcTomName() ;if ( (tomMatch105NameNumber_freshVar_24 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch105NameNumber_freshVar_24.getString() ;if (  tomMatch105NameNumber_freshVar_19.getTailconcTomName() .isEmptyconcTomName() ) { tom.engine.adt.tomtype.types.TomType  tom_userType= (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstType() ;


        TomSymbol symbol = getSymbolFromName(tom_name);
        String typeName = TomBase.getTomType(tom_userType);
        if(!testTypeExistence(typeName)) {// check that the type exists
          messageError(getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.unknownMatchArgumentTypeInSignature,
              new Object[]{tom_name, typeName });
        }          
        if(symbol != null) { // check that the type provided by the user is consistent
          TomType type = TomBase.getSymbolCodomain(symbol);
          if(!(tom_userType).equals(type)) {
            messageError(getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.inconsistentTypes,
                new Object[]{tom_name, TomBase.getTomType(type), TomBase.getTomType(tom_userType)});
          }
        }
        // a function call 
        return tom_userType;
      }}}}}}}}}

    return null;
  }
  
  /**
   * if a type is not specified 
   * 1. we look for a type in all match constraints where we can find this subject
   * 2. TODO: if the subject is in a constraint with a variable (the pattern is a variable for instance),
   * try to see if a variable with the same name already exists and can be typed, and if yes, get that type
   */
  private TomType guessSubjectType(TomTerm subject,ArrayList<Constraint> constraints) {
    for(Constraint constr:constraints) {
      {{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_s= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ;

          // we want two terms to be equal even if their option is different 
          //( because of their possition for example )
 matchL:  {{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAstName() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAstType() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstType() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getNameList() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getArgs() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getArgs() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getNameList() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getSlots() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getSlots() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomterm.types.TomList  tom_tomList= (( tom.engine.adt.tomterm.types.TomTerm )subject).getAttrList() ;if ( ( (( tom.engine.adt.tomterm.types.TomTerm )subject).getChildList() ==tom_tomList) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getNameList() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAttrList() ==tom_tomList) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getChildList() ==tom_tomList) ) {
 break matchL; }}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch107NameNumber_freshVar_46= (( tom.engine.adt.tomterm.types.TomTerm )subject).getTomTerm() ;if ( (tomMatch107NameNumber_freshVar_46 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch107NameNumber_freshVar_52= (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getTomTerm() ;if ( (tomMatch107NameNumber_freshVar_52 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( ( tomMatch107NameNumber_freshVar_52.getNameList() == tomMatch107NameNumber_freshVar_46.getNameList() ) ) {if ( ( tomMatch107NameNumber_freshVar_52.getArgs() == tomMatch107NameNumber_freshVar_46.getArgs() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAstType() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstType() ) ) {
break matchL;}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
 continue; }}}}

          TomTerm pattern =  (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() ;
          {{if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 pattern =  (( tom.engine.adt.tomterm.types.TomTerm )pattern).getTomTerm() ; }}}}{{if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch109NameNumber_freshVar_8= false ; tom.engine.adt.tomname.types.TomNameList  tomMatch109NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch109NameNumber_freshVar_8= true ;tomMatch109NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )pattern).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch109NameNumber_freshVar_8= true ;tomMatch109NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )pattern).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {{tomMatch109NameNumber_freshVar_8= true ;tomMatch109NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )pattern).getNameList() ;}}}}if ((tomMatch109NameNumber_freshVar_8 ==  true )) {if ( ((tomMatch109NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch109NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch109NameNumber_freshVar_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch109NameNumber_freshVar_7= tomMatch109NameNumber_freshVar_1.getHeadconcTomName() ;if ( (tomMatch109NameNumber_freshVar_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch109NameNumber_freshVar_7.getString() ;


        
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
                    messageError(getCurrentTomStructureOrgTrack().getFileName(),
                        getCurrentTomStructureOrgTrack().getLine(),
                        TomMessage.unknownMatchArgumentTypeInSignature,
                        new Object[]{tom_name, typeName});
                  }
                  return type;
                }
             }}}}}}}







         
        }}}}













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
  public static class CollectMatchConstraints extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  constrList;public CollectMatchConstraints( java.util.Collection  constrList) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;}public  java.util.Collection  getconstrList() {return constrList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {

        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));         
        throw new VisitFailure();// to stop the top-down
      }}}}return _visit_Constraint(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectMatchConstraints( java.util.Collection  t0) { return new CollectMatchConstraints(t0);}


 
  /**
   * Collect the constraints (match and numeric)
   * For Or constraints, collect also the associated action
   */
  public static class CollectConstraints extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  constrList;private  java.util.Collection  lastAction;private  java.util.HashMap  orActionMap;public CollectConstraints( java.util.Collection  constrList,  java.util.Collection  lastAction,  java.util.HashMap  orActionMap) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;this.lastAction=lastAction;this.orActionMap=orActionMap;}public  java.util.Collection  getconstrList() {return constrList;}public  java.util.Collection  getlastAction() {return lastAction;}public  java.util.HashMap  getorActionMap() {return orActionMap;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {


        lastAction.clear();
        lastAction.add( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getAction() );
      }}}}return _visit_ConstraintInstruction(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {boolean tomMatch112NameNumber_freshVar_2= false ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {tomMatch112NameNumber_freshVar_2= true ;} else {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {tomMatch112NameNumber_freshVar_2= true ;}}if ((tomMatch112NameNumber_freshVar_2 ==  true )) {


        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));         
        throw new VisitFailure();// to stop the top-down
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_oc=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);

        constrList.add(tom_oc);
        Iterator it = lastAction.iterator();
        orActionMap.put(tom_oc,it.next());
      }}}}return _visit_Constraint(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  _visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tominstruction.types.ConstraintInstruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {return ((T)visit_ConstraintInstruction((( tom.engine.adt.tominstruction.types.ConstraintInstruction )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectConstraints( java.util.Collection  t0,  java.util.Collection  t1,  java.util.HashMap  t2) { return new CollectConstraints(t0,t1,t2);}


  
  /**
   * check the lhs of a rule
   */
  private void verifyMatchPattern(TomTerm term, TomType type) {      
    // the term cannont be a Var* nor a _*
    TermDescription termDesc = analyseTerm(term);
    if(termDesc.getTermClass() == UNAMED_VARIABLE_STAR || termDesc.getTermClass() == VARIABLE_STAR) {
      messageError(termDesc.getFileName(),termDesc.getLine(), TomMessage.incorrectVariableStarInMatch, new Object[]{termDesc.getName()});
    } else {    
      // Analyse the term if type != null
      if(type != null) {
        // the type is known and found in the match signature
        validateTerm(term, type, false, true, false);
      }
    }
  }

  /*
   * verify the structure of a %strategy
   */
  private void verifyStrategy(TomVisitList visitList) throws VisitFailure {
    for(TomVisit visit:(tom.engine.adt.tomsignature.types.tomvisitlist.concTomVisit)visitList) {
      verifyVisit(visit);
    }
  }

  private void verifyVisit(TomVisit visit) throws VisitFailure {
    {{if ( (visit instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )visit) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= (( tom.engine.adt.tomsignature.types.TomVisit )visit).getAstConstraintInstructionList() ;
        
        ArrayList<MatchConstraint> matchConstraints = new ArrayList<MatchConstraint>();
        {{if ( (tom_constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tomMatch114NameNumber_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList);do {{if (!( tomMatch114NameNumber_end_4.isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch114NameNumber_freshVar_8= tomMatch114NameNumber_end_4.getHeadconcConstraintInstruction() ;if ( (tomMatch114NameNumber_freshVar_8 instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {

            matchConstraints.clear();
            tom_make_TopDownCollect(tom_make_CollectMatchConstraints(matchConstraints)).visitLight( tomMatch114NameNumber_freshVar_8.getConstraint() );   
            // for the first constraint, check that the type is conform to the type specified in visit
            MatchConstraint firstMatchConstr = matchConstraints.get(0); 
            verifyMatchPattern(firstMatchConstr.getPattern(),  (( tom.engine.adt.tomsignature.types.TomVisit )visit).getVNode() );
          }}if ( tomMatch114NameNumber_end_4.isEmptyconcConstraintInstruction() ) {tomMatch114NameNumber_end_4=(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList);} else {tomMatch114NameNumber_end_4= tomMatch114NameNumber_end_4.getTailconcConstraintInstruction() ;}}} while(!( (tomMatch114NameNumber_end_4==(( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_constraintInstructionList)) ));}}}}
    
        // check the rest of the constraints
        verifyMatch(tom_constraintInstructionList, (( tom.engine.adt.tomsignature.types.TomVisit )visit).getOption() );
      }}}}

  }

  private boolean findMakeDecl(OptionList option) {
    {{if ( (option instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )option) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )option) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch115NameNumber_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);do {{if (!( tomMatch115NameNumber_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch115NameNumber_freshVar_8= tomMatch115NameNumber_end_4.getHeadconcOption() ;if ( (tomMatch115NameNumber_freshVar_8 instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {if ( ( tomMatch115NameNumber_freshVar_8.getAstDeclaration()  instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {

        return true;
      }}}if ( tomMatch115NameNumber_end_4.isEmptyconcOption() ) {tomMatch115NameNumber_end_4=(( tom.engine.adt.tomoption.types.OptionList )option);} else {tomMatch115NameNumber_end_4= tomMatch115NameNumber_end_4.getTailconcOption() ;}}} while(!( (tomMatch115NameNumber_end_4==(( tom.engine.adt.tomoption.types.OptionList )option)) ));}}}}

    return false;
  }

  /**
   * Analyse a term given an expected type and re-enter recursively on children
   */
  public TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel, boolean permissive) {
    String termName = "emptyName";
    TomType type = null;
    int termClass = -1;
    String fileName = "unknown";
    int decLine = -1;
    Option orgTrack;
    matchblock:{
      {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch116NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( ((tomMatch116NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch116NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch116NameNumber_freshVar_2.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch116NameNumber_freshVar_8= tomMatch116NameNumber_freshVar_2.getHeadconcTomName() ;if ( (tomMatch116NameNumber_freshVar_8 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "".equals( tomMatch116NameNumber_freshVar_8.getString() ) ) {if (  tomMatch116NameNumber_freshVar_2.getTailconcTomName() .isEmptyconcTomName() ) {

          fileName = findOriginTrackingFileName(tom_options);
          decLine = findOriginTrackingLine(tom_options);
          termClass = UNAMED_APPL;
            // there shall be only one list symbol with expectedType as Codomain
            // else ensureValidUnamedList returns null
          TomSymbol symbol = ensureValidUnamedList(expectedType, fileName,decLine);
          if(symbol == null) {
            break matchblock;
          } else {
            // there is only one list symbol and its type is the expected one
            // (ensure by ensureValidUnamedList call)
            type = expectedType;
            termName = symbol.getAstName().getString();
              // whatever the arity is, we continue recursively and there is
              // only one element in the Domain
            validateListOperatorArgs( (( tom.engine.adt.tomterm.types.TomTerm )term).getArgs() , symbol.getTypesToType().getDomain().getHeadconcTomType(),
                symbol.getTypesToType().getCodomain(),permissive);
            if(permissive) { System.out.println("UnamedList but permissive");}
            break matchblock;
          }
        }}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ; tom.engine.adt.tomname.types.TomNameList  tom_symbolNameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;


          TomList args =  (( tom.engine.adt.tomterm.types.TomTerm )term).getArgs() ;
          fileName = findOriginTrackingFileName(tom_options);
          decLine = findOriginTrackingLine(tom_options);
          termClass = TERM_APPL;

          TomSymbol symbol = ensureValidApplDisjunction(tom_symbolNameList, expectedType, fileName, decLine, permissive, topLevel);

          if(symbol == null) {
            validateTermThrough(term,permissive);
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
            validateListOperatorArgs(args, symbol.getTypesToType().getDomain().getHeadconcTomType(),
                symbol.getTypesToType().getCodomain(),permissive);
          } else {
            // the arity is important also there are different types in Domain
            TomTypeList types = symbol.getTypesToType().getDomain();
            int nbArgs = args.length();
            int nbExpectedArgs = types.length();
            if(nbArgs != nbExpectedArgs) {
              messageError(fileName, decLine, TomMessage.symbolNumberArgument,
                  new Object[]{termName, Integer.valueOf(nbExpectedArgs), Integer.valueOf(nbArgs)});
              break matchblock;
            }
            while(!args.isEmptyconcTomTerm()) {
                // repeat analyse with associated expected type and control
                // arity
              validateTerm(args.getHeadconcTomTerm(), types.getHeadconcTomType(), listOp/* false */, false, permissive);
              args = args.getTailconcTomTerm();
              types = types.getTailconcTomType();
            }
          }
          break matchblock;
        }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ; tom.engine.adt.tomname.types.TomNameList  tom_symbolNameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomslot.types.SlotList  tom_slotList= (( tom.engine.adt.tomterm.types.TomTerm )term).getSlots() ;


          if(permissive) {
            // Record are not allowed in a rhs
            messageError(findOriginTrackingFileName(tom_options),findOriginTrackingLine(tom_options), TomMessage.incorrectRuleRHSClass, new Object[]{getName((( tom.engine.adt.tomterm.types.TomTerm )term))+"[...]"});
          }
          fileName = findOriginTrackingFileName(tom_options);
          decLine = findOriginTrackingLine(tom_options);
          termClass = RECORD_APPL;
          TomSymbol symbol = ensureValidRecordDisjunction(tom_symbolNameList, tom_slotList, expectedType, fileName, decLine, true);
          if(symbol == null) {
            break matchblock;
          }

          {{if ( (tom_symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch117NameNumber_end_4=(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList);do {{if (!( tomMatch117NameNumber_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch117NameNumber_freshVar_8= tomMatch117NameNumber_end_4.getHeadconcTomName() ;if ( (tomMatch117NameNumber_freshVar_8 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {





              verifyRecordStructure(tom_options,  tomMatch117NameNumber_freshVar_8.getString() , tom_slotList, fileName,decLine);
            }}if ( tomMatch117NameNumber_end_4.isEmptyconcTomName() ) {tomMatch117NameNumber_end_4=(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList);} else {tomMatch117NameNumber_end_4= tomMatch117NameNumber_end_4.getTailconcTomName() ;}}} while(!( (tomMatch117NameNumber_end_4==(( tom.engine.adt.tomname.types.TomNameList )tom_symbolNameList)) ));}}}}


          type = expectedType;
          TomName headName = tom_symbolNameList.getHeadconcTomName();
          if(headName instanceof AntiName) {
        	  headName = ((AntiName)headName).getName();
          }
          termName = headName.getString();
          break matchblock;
        }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch116NameNumber_freshVar_22= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( ((tomMatch116NameNumber_freshVar_22 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch116NameNumber_freshVar_22 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch116NameNumber_end_28=tomMatch116NameNumber_freshVar_22;do {{if (!( tomMatch116NameNumber_end_28.isEmptyconcTomName() )) {if ( ( tomMatch116NameNumber_end_28.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


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
           * symbolTable().getType(Constants.TNODE); because TNodeType should be
           * a TomTypeAlone and not an expanded type
           */
          TomType TNodeType = TomBase.getSymbolCodomain(symbolTable().getSymbolFromName(Constants.ELEMENT_NODE));
          // System.out.println("TNodeType = " + TNodeType);
          while(!args.isEmptyconcTomTerm()) {
            // repeat analyse with associated expected type and control arity
            validateTerm(args.getHeadconcTomTerm(), TNodeType, true, false, permissive);
            args = args.getTailconcTomTerm();
          }

          break matchblock;
        }}if ( tomMatch116NameNumber_end_28.isEmptyconcTomName() ) {tomMatch116NameNumber_end_28=tomMatch116NameNumber_freshVar_22;} else {tomMatch116NameNumber_end_28= tomMatch116NameNumber_end_28.getTailconcTomName() ;}}} while(!( (tomMatch116NameNumber_end_28==tomMatch116NameNumber_freshVar_22) ));}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch116NameNumber_freshVar_35= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( (tomMatch116NameNumber_freshVar_35 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


          termClass = VARIABLE;
          fileName = findOriginTrackingFileName(tom_options);
          decLine = findOriginTrackingLine(tom_options);
          type = null;
          termName =  tomMatch116NameNumber_freshVar_35.getString() ;
          break matchblock;
        }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch116NameNumber_freshVar_41= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( (tomMatch116NameNumber_freshVar_41 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


          termClass = VARIABLE_STAR;
          fileName = findOriginTrackingFileName(tom_options);
          decLine = findOriginTrackingLine(tom_options);
          type = null;
          termName =  tomMatch116NameNumber_freshVar_41.getString() +"*";
          if(!listSymbol) {
            messageError(fileName,decLine, TomMessage.invalidVariableStarArgument, new Object[]{termName});
          }
          break matchblock;
        }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;


          termClass = UNAMED_VARIABLE;
          fileName = findOriginTrackingFileName(tom_options);
          decLine = findOriginTrackingLine(tom_options);
          type = null;
          termName = "_";
          if(permissive) {
            messageError(fileName,decLine, TomMessage.incorrectRuleRHSClass, new Object[]{termName});
          }
          break matchblock;
        }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;


          termClass = UNAMED_VARIABLE_STAR;
          fileName = findOriginTrackingFileName(tom_options);
          decLine = findOriginTrackingLine(tom_options);
          type = null;
          termName = "_*";
          if(!listSymbol) {
            messageError(fileName,decLine, TomMessage.invalidVariableStarArgument, new Object[]{termName});
          }
          if(permissive) {
            messageError(fileName,decLine, TomMessage.incorrectRuleRHSClass, new Object[]{termName});
          }
          break matchblock;
        }}}}

      throw new TomRuntimeException("Strange Term "+term);
    }
    return new TermDescription(termClass, termName, fileName,decLine, type);
  }

  private void validateTermThrough(TomTerm term, boolean permissive) {
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch118NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getArgs() ;if ( ((tomMatch118NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch118NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch118NameNumber_end_6=tomMatch118NameNumber_freshVar_1;do {{if (!( tomMatch118NameNumber_end_6.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tom_child= tomMatch118NameNumber_end_6.getHeadconcTomTerm() ;

        TomSymbol sym = getSymbolFromName(getName(tom_child));
        if(sym != null) {
          validateTerm(tom_child,sym.getTypesToType().getCodomain(),false,false,permissive);
        } else {
          validateTermThrough(tom_child,permissive);
        }
      }if ( tomMatch118NameNumber_end_6.isEmptyconcTomTerm() ) {tomMatch118NameNumber_end_6=tomMatch118NameNumber_freshVar_1;} else {tomMatch118NameNumber_end_6= tomMatch118NameNumber_end_6.getTailconcTomTerm() ;}}} while(!( (tomMatch118NameNumber_end_6==tomMatch118NameNumber_freshVar_1) ));}}}}}

  }

  public TermDescription analyseTerm(TomTerm term) {
    matchblock:{
      {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch119NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( ((tomMatch119NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch119NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch119NameNumber_freshVar_2.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch119NameNumber_freshVar_7= tomMatch119NameNumber_freshVar_2.getHeadconcTomName() ;if ( (tomMatch119NameNumber_freshVar_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_str= tomMatch119NameNumber_freshVar_7.getString() ;if (  tomMatch119NameNumber_freshVar_2.getTailconcTomName() .isEmptyconcTomName() ) {

          if (tom_str.equals("")) {
            return new TermDescription(UNAMED_APPL, tom_str,
                findOriginTrackingFileName(tom_options),
                findOriginTrackingLine(tom_options), 
                null);
              // TODO
          } else {
            return new TermDescription(TERM_APPL, tom_str,
                findOriginTrackingFileName(tom_options),
                findOriginTrackingLine(tom_options),
                TomBase.getSymbolCodomain(getSymbolFromName(tom_str)));
          }
        }}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch119NameNumber_freshVar_10= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( ((tomMatch119NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch119NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch119NameNumber_freshVar_10.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch119NameNumber_freshVar_16= tomMatch119NameNumber_freshVar_10.getHeadconcTomName() ;if ( (tomMatch119NameNumber_freshVar_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch119NameNumber_freshVar_16.getString() ;

          return new TermDescription(APPL_DISJUNCTION, tom_name,
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),
              TomBase.getSymbolCodomain(getSymbolFromName(tom_name)));
        }}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch119NameNumber_freshVar_19= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( ((tomMatch119NameNumber_freshVar_19 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch119NameNumber_freshVar_19 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch119NameNumber_freshVar_19.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch119NameNumber_freshVar_24= tomMatch119NameNumber_freshVar_19.getHeadconcTomName() ;if ( (tomMatch119NameNumber_freshVar_24 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch119NameNumber_freshVar_24.getString() ;if (  tomMatch119NameNumber_freshVar_19.getTailconcTomName() .isEmptyconcTomName() ) {

          return new TermDescription(RECORD_APPL, tom_name,
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),
              TomBase.getSymbolCodomain(getSymbolFromName(tom_name)));
        }}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch119NameNumber_freshVar_27= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( ((tomMatch119NameNumber_freshVar_27 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch119NameNumber_freshVar_27 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch119NameNumber_freshVar_27.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch119NameNumber_freshVar_33= tomMatch119NameNumber_freshVar_27.getHeadconcTomName() ;if ( (tomMatch119NameNumber_freshVar_33 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch119NameNumber_freshVar_33.getString() ;

          return new TermDescription(RECORD_APPL_DISJUNCTION,tom_name,
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),
              TomBase.getSymbolCodomain(getSymbolFromName(tom_name)));
        }}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;

          return new TermDescription(XML_APPL, Constants.ELEMENT_NODE,
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),
              TomBase.getSymbolCodomain(getSymbolFromName(Constants.ELEMENT_NODE)));
        }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch119NameNumber_freshVar_39= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( (tomMatch119NameNumber_freshVar_39 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

          return new TermDescription(VARIABLE,  tomMatch119NameNumber_freshVar_39.getString() ,
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),  null);
        }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch119NameNumber_freshVar_45= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;if ( (tomMatch119NameNumber_freshVar_45 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

          return new TermDescription(VARIABLE_STAR,  tomMatch119NameNumber_freshVar_45.getString() +"*",
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),  null);
        }}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;

          return new TermDescription(UNAMED_VARIABLE, "_",
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),  null);
        }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) { tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )term).getOption() ;

          return new TermDescription(UNAMED_VARIABLE_STAR, "_*",
                findOriginTrackingFileName(tom_options),
              findOriginTrackingLine(tom_options),  null);
        }}}}

      throw new TomRuntimeException("Strange Term "+term);
    }
  }

  private TomSymbol ensureValidUnamedList(TomType expectedType, String fileName,int decLine) {
    TomSymbolList symbolList = symbolTable().getSymbolFromType(expectedType);
    TomSymbolList filteredList =  tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() ;
    {{if ( (symbolList instanceof tom.engine.adt.tomsignature.types.TomSymbolList) ) {if ( (((( tom.engine.adt.tomsignature.types.TomSymbolList )symbolList) instanceof tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol) || ((( tom.engine.adt.tomsignature.types.TomSymbolList )symbolList) instanceof tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol)) ) { tom.engine.adt.tomsignature.types.TomSymbolList  tomMatch120NameNumber_end_4=(( tom.engine.adt.tomsignature.types.TomSymbolList )symbolList);do {{if (!( tomMatch120NameNumber_end_4.isEmptyconcTomSymbol() )) { tom.engine.adt.tomsignature.types.TomSymbol  tom_symbol= tomMatch120NameNumber_end_4.getHeadconcTomSymbol() ;

        if(TomBase.isArrayOperator(tom_symbol) || TomBase.isListOperator(tom_symbol)) {
          filteredList =  tom.engine.adt.tomsignature.types.tomsymbollist.ConsconcTomSymbol.make(tom_symbol,tom_append_list_concTomSymbol(filteredList, tom.engine.adt.tomsignature.types.tomsymbollist.EmptyconcTomSymbol.make() )) ;
        }
      }if ( tomMatch120NameNumber_end_4.isEmptyconcTomSymbol() ) {tomMatch120NameNumber_end_4=(( tom.engine.adt.tomsignature.types.TomSymbolList )symbolList);} else {tomMatch120NameNumber_end_4= tomMatch120NameNumber_end_4.getTailconcTomSymbol() ;}}} while(!( (tomMatch120NameNumber_end_4==(( tom.engine.adt.tomsignature.types.TomSymbolList )symbolList)) ));}}}}


    if(filteredList.isEmptyconcTomSymbol()) {
      messageError(fileName,decLine,
                   TomMessage.unknownUnamedList,
                   new Object[]{expectedType.getString()});
      return null;
    } else if(!filteredList.getTailconcTomSymbol().isEmptyconcTomSymbol()) {
      StringBuilder symbolsString = new StringBuilder();
      while(!filteredList.isEmptyconcTomSymbol()) {
        symbolsString .append(" " + filteredList.getHeadconcTomSymbol().getAstName().getString());
        filteredList= filteredList.getTailconcTomSymbol();
      }
      messageError(fileName,decLine,
                   TomMessage.ambigousUnamedList,
                   new Object[]{expectedType.getString(), symbolsString.toString()});
      return null;
    } else {
      return filteredList.getHeadconcTomSymbol();
    }
  }

  private TomSymbol ensureValidApplDisjunction(TomNameList symbolNameList, TomType expectedType, 
      String fileName, int decLine, boolean permissive, boolean topLevel) {

    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);
      if (symbol == null ) {
        // this correspond to a term like 'unknown()' or unknown(s1, s2, ...)
        if(!permissive) {
          messageError(fileName,decLine, TomMessage.unknownSymbol, new Object[]{res});
        } else {
          messageWarning(fileName,decLine, TomMessage.unknownPermissiveSymbol, new Object[]{res});
        }
      } else { // known symbol
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
            return null;
          }
        }
      }
      return symbol;
    } else {
      // this is a disjunction
      if(permissive) {
	messageError(fileName,decLine, TomMessage.impossiblePermissiveAndDisjunction, new Object[]{});
      }

      TomSymbol symbol = null;
      TomTypeList domainReference = null;
      PairNameDeclList slotReference = null;
      String nameReference = null;
      {{if ( (symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch121NameNumber_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);do {{if (!( tomMatch121NameNumber_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch121NameNumber_freshVar_8= tomMatch121NameNumber_end_4.getHeadconcTomName() ;if ( (tomMatch121NameNumber_freshVar_8 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_dijName= tomMatch121NameNumber_freshVar_8.getString() ;
 // for each SymbolName
          symbol =  getSymbolFromName(tom_dijName);
          if(symbol == null) {
            // In disjunction we can only have known symbols
            messageError(fileName,decLine, TomMessage.unknownSymbolInDisjunction, new Object[]{(tom_dijName)});
            return null;
          }
          if( strictType  || !topLevel ) {
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
              messageError(fileName,decLine, TomMessage.invalidDisjunctionDomain, new Object[]{nameReference, (tom_dijName) });
              return null;
            }
            if(symbol.getPairNameDeclList() != slotReference) {
              PairNameDeclList l1 = slotReference;
              PairNameDeclList l2 = symbol.getPairNameDeclList();
              while(!l1.isEmptyconcPairNameDecl()) {
                if(l1.getHeadconcPairNameDecl().getSlotName() != l2.getHeadconcPairNameDecl().getSlotName()) {
                  messageError(fileName,decLine, TomMessage.invalidDisjunctionDomain, new Object[]{nameReference, (tom_dijName) });
                  return null;
                }
                l1=l1.getTailconcPairNameDecl();
                l2=l2.getTailconcPairNameDecl();
              }
            }
          }
        }}if ( tomMatch121NameNumber_end_4.isEmptyconcTomName() ) {tomMatch121NameNumber_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);} else {tomMatch121NameNumber_end_4= tomMatch121NameNumber_end_4.getTailconcTomName() ;}}} while(!( (tomMatch121NameNumber_end_4==(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) ));}}}}

      return symbol;
    }
  }

  private boolean ensureSymbolCodomain(TomType currentCodomain, TomType expectedType, TomMessage msg, String symbolName, String fileName,int decLine) {
    if(currentCodomain != expectedType) {
      // System.out.println(currentCodomain+"!="+expectedType);
      messageError(fileName,decLine,
                   msg,
                   new Object[]{symbolName, currentCodomain.getString(), expectedType.getString()});
      return false;
    }
    return true;
  }

  private TomSymbol ensureValidRecordDisjunction(TomNameList symbolNameList, SlotList slotList, 
      TomType expectedType, String fileName, int decLine, boolean topLevel) {
    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol =  getSymbolFromName(res);
      if (symbol == null ) { // this correspond to: unknown[]
          // it is not correct to use Record with unknown symbols
        messageError(fileName,decLine, TomMessage.unknownSymbol, new Object[]{res});
        return null;
      } else { // known symbol
          // ensure type correctness if necessary
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
            return null;
          }
        }
      }
      return symbol;
    } else {
      TomSymbol symbol = null;
      TomSymbol referenceSymbol = null;
      TomTypeList referenceDomain = null;
      String referenceName = null;
      {{if ( (symbolNameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )symbolNameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch122NameNumber_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);do {{if (!( tomMatch122NameNumber_end_4.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch122NameNumber_freshVar_8= tomMatch122NameNumber_end_4.getHeadconcTomName() ;if ( (tomMatch122NameNumber_freshVar_8 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_dijName= tomMatch122NameNumber_freshVar_8.getString() ;
 // for each SymbolName
          symbol =  getSymbolFromName(tom_dijName);
          if(symbol == null) {
            // In disjunction we can only have known symbols
            messageError(fileName,decLine, TomMessage.unknownSymbolInDisjunction, new Object[]{(tom_dijName)});
            return null;
          }
          if( strictType  || !topLevel ) {
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
                messageError(fileName,decLine, TomMessage.invalidDisjunctionSlotName, new Object[]{referenceName,((Name)slotName).getString()});
                return null;                
              }
              
              if (currentSlotIndex == -1){
                messageError(fileName,decLine, TomMessage.invalidDisjunctionSlotName, new Object[]{(tom_dijName),((Name)slotName).getString() });
                return null;                
              }
              
              if(TomBase.elementAt(currentDomain,currentSlotIndex) != TomBase.elementAt(referenceDomain,referenceSlotIndex)) {
                messageError(fileName,decLine, TomMessage.invalidDisjunctionDomain, new Object[]{referenceName, (tom_dijName) });
                return null;
              }

              slotList = slotList.getTailconcSlot();
            }

          }
        }}if ( tomMatch122NameNumber_end_4.isEmptyconcTomName() ) {tomMatch122NameNumber_end_4=(( tom.engine.adt.tomname.types.TomNameList )symbolNameList);} else {tomMatch122NameNumber_end_4= tomMatch122NameNumber_end_4.getTailconcTomName() ;}}} while(!( (tomMatch122NameNumber_end_4==(( tom.engine.adt.tomname.types.TomNameList )symbolNameList)) ));}}}}

      return symbol;
    }
  }

  // /////////////////////
  // RECORDS CONCERNS ///
  // /////////////////////
  private void verifyRecordStructure(OptionList option, String tomName, SlotList slotList, String fileName, int decLine)  {
    TomSymbol symbol = getSymbolFromName(tomName);
    if(symbol != null) {
        // constants have an emptyPairNameDeclList
        // the length of the pairNameDeclList corresponds to the arity of the
        // operator
        // list operator with [] no allowed
      if(slotList.isEmptyconcSlot() && (TomBase.isListOperator(symbol) ||  TomBase.isArrayOperator(symbol)) ) {
        messageError(fileName,decLine,
                     TomMessage.bracketOnListSymbol,
                     new Object[]{tomName});
      }
        // TODO verify type
      verifyRecordSlots(slotList,symbol, TomBase.getSymbolDomain(symbol), tomName, fileName, decLine);
    } else {
      messageError(fileName,decLine,
                   TomMessage.unknownSymbol,
                   new Object[]{tomName});
    }
  }

    // We test the existence/repetition of slotName contained in pairSlotAppl
    // and then the associated term
  private void verifyRecordSlots(SlotList slotList, TomSymbol tomSymbol, TomTypeList typeList, String methodName, String fileName, int decLine) {
  TomName pairSlotName = null;
  ArrayList<String> listOfPossibleSlot = null;
  ArrayList<Integer> studiedSlotIndexList = new ArrayList<Integer>();
    // for each pair slotName <=> Appl
  while( !slotList.isEmptyconcSlot() ) {
      pairSlotName = slotList.getHeadconcSlot().getSlotName();
        // First check for slot name correctness
      int index = TomBase.getSlotIndex(tomSymbol,pairSlotName);
      if(index < 0) {// Error: bad slot name
        if(listOfPossibleSlot == null) {
          // calculate list of possible slot names..
          listOfPossibleSlot = new ArrayList<String>();
          PairNameDeclList listOfSlots = tomSymbol.getPairNameDeclList();
          while ( !listOfSlots.isEmptyconcPairNameDecl() ) {
            TomName sname = listOfSlots.getHeadconcPairNameDecl().getSlotName();
            if(!sname.isEmptyName()) {
              listOfPossibleSlot.add(sname.getString());
            }
            listOfSlots = listOfSlots.getTailconcPairNameDecl();
          }
        }
        messageError(fileName,decLine,
                     TomMessage.badSlotName,
                     new Object[]{pairSlotName.getString(), methodName, listOfPossibleSlot.toString()});
        return; // break analyses
      } else { // then check for repeated good slot name
        Integer integerIndex = Integer.valueOf(index);
        if(studiedSlotIndexList.contains(integerIndex)) {
            // Error: repeated slot
          messageError(fileName,decLine,
                       TomMessage.slotRepeated,
                       new Object[]{methodName, pairSlotName.getString()});
          return; // break analyses
        }
        studiedSlotIndexList.add(integerIndex);
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
              {{if ( (slotName instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )slotName) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (pairSlotTerm instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )pairSlotTerm) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch123NameNumber_freshVar_4= (( tom.engine.adt.tomslot.types.Slot )pairSlotTerm).getSlotName() ;if ( (tomMatch123NameNumber_freshVar_4 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch123NameNumber_freshVar_4.getString() .equals( (( tom.engine.adt.tomname.types.TomName )slotName).getString() ) ) {

                   validateTerm( (( tom.engine.adt.tomslot.types.Slot )pairSlotTerm).getAppl() ,expectedType, false, true, false);
                   break whileBlock;
                 }}}}}}}{if ( (slotName instanceof tom.engine.adt.tomname.types.TomName) ) {if ( (pairSlotTerm instanceof tom.engine.adt.tomslot.types.Slot) ) {
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
  }

  public void validateListOperatorArgs(TomList args, TomType expectedType, TomType parentListCodomain, boolean permissive) {
    while(!args.isEmptyconcTomTerm()) {
      TomTerm currentArg = args.getHeadconcTomTerm();      
      TomSymbol argSymbol = getSymbolFromName(getName(currentArg));
      // if we have a sublist 
      if(TomBase.isListOperator(argSymbol)) {
        // we can have two cases:
        // 1. the sublist has the codomain = parentListCodomain
        // 2. the sublist has the codomain = expectedType
        if(argSymbol.getTypesToType().getCodomain() == parentListCodomain) {
          validateTerm(currentArg, parentListCodomain, true, false, permissive);            
        } else {
          validateTerm(currentArg, expectedType, true, false, permissive);    
        }        
      } else {
        validateTerm(currentArg, expectedType, true, false, permissive);
      }
      args = args.getTailconcTomTerm();
    }
  }

  private boolean testTypeExistence(String typeName) {
    return symbolTable().getType(typeName) != null;
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
  /**
    * verify a BackQuoteAppl term
    * @param subject the backquote term
    * 1. arity
    */
  private void verifyBackQuoteAppl(TomTerm subject) throws VisitFailure {
    {{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.BackQuoteAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch124NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_options= (( tom.engine.adt.tomterm.types.TomTerm )subject).getOption() ;if ( (tomMatch124NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch124NameNumber_freshVar_2.getString() ;

        int decLine = findOriginTrackingLine(tom_options);
        String fileName = findOriginTrackingFileName(tom_options);
        TomSymbol symbol = getSymbolFromName(tom_name);
        if(symbol==null) {
          // cannot repport an error because unknown funtion calls are allowed
          //messageError(fileName, decLine, TomMessage.unknownSymbol, new Object[]{`(name)});
        } else {
          //System.out.println("symbol = " + symbol);
          // check the arity (for syntacti operators)
          if(!TomBase.isArrayOperator(symbol) && !TomBase.isListOperator(symbol)) {
            TomTypeList types = symbol.getTypesToType().getDomain();
            int nbExpectedArgs = types.length();
            int nbArgs =  (( tom.engine.adt.tomterm.types.TomTerm )subject).getArgs() .length();
            if(nbArgs != nbExpectedArgs) {
              messageError(fileName, decLine, TomMessage.symbolNumberArgument,
                  new Object[]{tom_name, Integer.valueOf(nbExpectedArgs), Integer.valueOf(nbArgs)});
            }
          }
        }
      }}}}}

  }

}
