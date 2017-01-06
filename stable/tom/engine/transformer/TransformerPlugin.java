/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Jean-Christophe Bach  e-mail: jeanchristophe.bach@inria.fr
 *
 **/

package tom.engine.transformer;

import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File;
import java.io.IOException;

import tom.engine.exception.TomException;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tominstruction.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomdeclaration.types.declaration.*;
import tom.engine.adt.tomdeclaration.types.declarationlist.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomoption.types.option.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.code.types.bqtermlist.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;

import tom.library.sl.*;

/**
 * The Transmorfer plugin.
 * Performs tree transformation and code expansion.
 *
 * TO BE DETAILED /!\
 * 1st step: Transformation - 
 * 
 * 
 * 
 */
public class TransformerPlugin extends TomGenericPlugin {

        private static   tom.engine.adt.tomsignature.types.ResolveStratBlockList  tom_append_list_concResolveStratBlock( tom.engine.adt.tomsignature.types.ResolveStratBlockList l1,  tom.engine.adt.tomsignature.types.ResolveStratBlockList  l2) {     if( l1.isEmptyconcResolveStratBlock() ) {       return l2;     } else if( l2.isEmptyconcResolveStratBlock() ) {       return l1;     } else if(  l1.getTailconcResolveStratBlock() .isEmptyconcResolveStratBlock() ) {       return  tom.engine.adt.tomsignature.types.resolvestratblocklist.ConsconcResolveStratBlock.make( l1.getHeadconcResolveStratBlock() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.resolvestratblocklist.ConsconcResolveStratBlock.make( l1.getHeadconcResolveStratBlock() ,tom_append_list_concResolveStratBlock( l1.getTailconcResolveStratBlock() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.ResolveStratBlockList  tom_get_slice_concResolveStratBlock( tom.engine.adt.tomsignature.types.ResolveStratBlockList  begin,  tom.engine.adt.tomsignature.types.ResolveStratBlockList  end, tom.engine.adt.tomsignature.types.ResolveStratBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcResolveStratBlock()  ||  (end== tom.engine.adt.tomsignature.types.resolvestratblocklist.EmptyconcResolveStratBlock.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.resolvestratblocklist.ConsconcResolveStratBlock.make( begin.getHeadconcResolveStratBlock() ,( tom.engine.adt.tomsignature.types.ResolveStratBlockList )tom_get_slice_concResolveStratBlock( begin.getTailconcResolveStratBlock() ,end,tail)) ;   }      private static   tom.engine.adt.tomsignature.types.ElementaryTransformationList  tom_append_list_concElementaryTransformation( tom.engine.adt.tomsignature.types.ElementaryTransformationList l1,  tom.engine.adt.tomsignature.types.ElementaryTransformationList  l2) {     if( l1.isEmptyconcElementaryTransformation() ) {       return l2;     } else if( l2.isEmptyconcElementaryTransformation() ) {       return l1;     } else if(  l1.getTailconcElementaryTransformation() .isEmptyconcElementaryTransformation() ) {       return  tom.engine.adt.tomsignature.types.elementarytransformationlist.ConsconcElementaryTransformation.make( l1.getHeadconcElementaryTransformation() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.elementarytransformationlist.ConsconcElementaryTransformation.make( l1.getHeadconcElementaryTransformation() ,tom_append_list_concElementaryTransformation( l1.getTailconcElementaryTransformation() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.ElementaryTransformationList  tom_get_slice_concElementaryTransformation( tom.engine.adt.tomsignature.types.ElementaryTransformationList  begin,  tom.engine.adt.tomsignature.types.ElementaryTransformationList  end, tom.engine.adt.tomsignature.types.ElementaryTransformationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcElementaryTransformation()  ||  (end== tom.engine.adt.tomsignature.types.elementarytransformationlist.EmptyconcElementaryTransformation.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.elementarytransformationlist.ConsconcElementaryTransformation.make( begin.getHeadconcElementaryTransformation() ,( tom.engine.adt.tomsignature.types.ElementaryTransformationList )tom_get_slice_concElementaryTransformation( begin.getTailconcElementaryTransformation() ,end,tail)) ;   }      private static   tom.engine.adt.tomsignature.types.ResolveStratElementList  tom_append_list_concResolveStratElement( tom.engine.adt.tomsignature.types.ResolveStratElementList l1,  tom.engine.adt.tomsignature.types.ResolveStratElementList  l2) {     if( l1.isEmptyconcResolveStratElement() ) {       return l2;     } else if( l2.isEmptyconcResolveStratElement() ) {       return l1;     } else if(  l1.getTailconcResolveStratElement() .isEmptyconcResolveStratElement() ) {       return  tom.engine.adt.tomsignature.types.resolvestratelementlist.ConsconcResolveStratElement.make( l1.getHeadconcResolveStratElement() ,l2) ;     } else {       return  tom.engine.adt.tomsignature.types.resolvestratelementlist.ConsconcResolveStratElement.make( l1.getHeadconcResolveStratElement() ,tom_append_list_concResolveStratElement( l1.getTailconcResolveStratElement() ,l2)) ;     }   }   private static   tom.engine.adt.tomsignature.types.ResolveStratElementList  tom_get_slice_concResolveStratElement( tom.engine.adt.tomsignature.types.ResolveStratElementList  begin,  tom.engine.adt.tomsignature.types.ResolveStratElementList  end, tom.engine.adt.tomsignature.types.ResolveStratElementList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcResolveStratElement()  ||  (end== tom.engine.adt.tomsignature.types.resolvestratelementlist.EmptyconcResolveStratElement.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomsignature.types.resolvestratelementlist.ConsconcResolveStratElement.make( begin.getHeadconcResolveStratElement() ,( tom.engine.adt.tomsignature.types.ResolveStratElementList )tom_get_slice_concResolveStratElement( begin.getTailconcResolveStratElement() ,end,tail)) ;   }      private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_append_list_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList l1,  tom.engine.adt.tomdeclaration.types.DeclarationList  l2) {     if( l1.isEmptyconcDeclaration() ) {       return l2;     } else if( l2.isEmptyconcDeclaration() ) {       return l1;     } else if(  l1.getTailconcDeclaration() .isEmptyconcDeclaration() ) {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,l2) ;     } else {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,tom_append_list_concDeclaration( l1.getTailconcDeclaration() ,l2)) ;     }   }   private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_get_slice_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList  begin,  tom.engine.adt.tomdeclaration.types.DeclarationList  end, tom.engine.adt.tomdeclaration.types.DeclarationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcDeclaration()  ||  (end== tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( begin.getHeadconcDeclaration() ,( tom.engine.adt.tomdeclaration.types.DeclarationList )tom_get_slice_concDeclaration( begin.getTailconcDeclaration() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {     if( l1.isEmptyconcTypeOption() ) {       return l2;     } else if( l2.isEmptyconcTypeOption() ) {       return l1;     } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.RuleInstructionList  tom_append_list_concRuleInstruction( tom.engine.adt.tominstruction.types.RuleInstructionList l1,  tom.engine.adt.tominstruction.types.RuleInstructionList  l2) {     if( l1.isEmptyconcRuleInstruction() ) {       return l2;     } else if( l2.isEmptyconcRuleInstruction() ) {       return l1;     } else if(  l1.getTailconcRuleInstruction() .isEmptyconcRuleInstruction() ) {       return  tom.engine.adt.tominstruction.types.ruleinstructionlist.ConsconcRuleInstruction.make( l1.getHeadconcRuleInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.ruleinstructionlist.ConsconcRuleInstruction.make( l1.getHeadconcRuleInstruction() ,tom_append_list_concRuleInstruction( l1.getTailconcRuleInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.RuleInstructionList  tom_get_slice_concRuleInstruction( tom.engine.adt.tominstruction.types.RuleInstructionList  begin,  tom.engine.adt.tominstruction.types.RuleInstructionList  end, tom.engine.adt.tominstruction.types.RuleInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcRuleInstruction()  ||  (end== tom.engine.adt.tominstruction.types.ruleinstructionlist.EmptyconcRuleInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.ruleinstructionlist.ConsconcRuleInstruction.make( begin.getHeadconcRuleInstruction() ,( tom.engine.adt.tominstruction.types.RuleInstructionList )tom_get_slice_concRuleInstruction( begin.getTailconcRuleInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {     if( l1.isEmptyconcPairNameDecl() ) {       return l2;     } else if( l2.isEmptyconcPairNameDecl() ) {       return l1;     } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}     






  private static Logger logger = Logger.getLogger("tom.engine.transformer.TransformerPlugin");
  /** some prefixes and suffixes */
  public static final String TRANSFORMED_SUFFIX = ".tfix.transformed";
  public static final String REFCLASS_PREFIX = "tom__reference_class_";
  public static final String STRAT_RESOLVE_PREFIX = "tom__StratResolve_";
  public static final String RESOLVE_INVERSE_LINK_FUNCTION = "resolveInverseLinks";
  public static final String RESOLVE_ELEMENT_PREFIX = "Resolve";
  public static final String RESOLVE_ELEMENT_ATTRIBUTE_NAME = "tom_resolve_element_attribute_name";
  public static final String RESOLVE_ELEMENT_ATTRIBUTE_O = "tom_resolve_element_attribute_o";
  private static final TomType voidType = ASTFactory.makeType( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,"undefined","void");

  private SymbolTable symbolTable;
  private int freshCounter = 0;
  private boolean resolveFlag = false;

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  public TransformerPlugin() {
    super("TransformerPlugin");
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    Code transformedTerm = (Code)getWorkingTerm();
    setSymbolTable(getStreamManager().getSymbolTable());
    try {
      // replace content of Transformation declaration by Strategy
      transformedTerm = tom_make_TopDown(tom_make_ProcessTransformation(this)).visitLight(transformedTerm);
      setWorkingTerm(transformedTerm);
      // verbose
      TomMessage.info(logger,null,0,TomMessage.tomTransformingPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() +
            TransformerPlugin.TRANSFORMED_SUFFIX, transformedTerm);
      }
    }  catch(VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.process: fail on " + transformedTerm);
    } catch (Exception e) {
      TomMessage.error(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }
  }

  /** 
   * This strategy handles each Transformation declaration and transforms it to
   * an AbstractDecl containing a list of declarations.
   */
  public static class ProcessTransformation extends tom.library.sl.AbstractStrategyBasic {private  TransformerPlugin  transformer;public ProcessTransformation( TransformerPlugin  transformer) {super(( new tom.library.sl.Identity() ));this.transformer=transformer;}public  TransformerPlugin  gettransformer() {return transformer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.Transformation) ) {







         return  tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl.make(transformer.processSubDecl((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg), (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getTName() , (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getDomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getElementaryTList() , (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getFileFrom() , (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getFileTo() , (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() )) ;
      }}}}return _visit_Declaration(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ProcessTransformation( TransformerPlugin  t0) { return new ProcessTransformation(t0);}



  /**
   * This function processes a transformation declarations list.
   * @param transfo the transformation Declaration
   * @param transfoName the name of the transformation
   * @param transfoDomain the domain of the transformation
   * @param elemTransfoList the list of elementary transformations composing
   * the whole transformation
   * @param fileFrom the source metamodel file name
   * @param fileTo the target metamodel file name
   * @param orgTrack an option to track the transformation construct
   * @return a list of declarations related to the transformation
   */
  private DeclarationList processSubDecl(Declaration transfo,TomName transfoName, TomTypeList transfoDomain, ElementaryTransformationList elemTransfoList, String fileFrom, String fileTo, Option orgTrack) {

    List<Declaration> result = new LinkedList<Declaration>();
    HashSet<String> resolveNameSet = new HashSet<String>();

    HashSet<String> withNameSet = new HashSet<String>();
    HashSet<String> toNameSet = new HashSet<String>();

    ElementaryTransformationList etl =  tom.engine.adt.tomsignature.types.elementarytransformationlist.EmptyconcElementaryTransformation.make() ;
    try {
      //This strategy call makes lots of side effects:
      //- on resolveNameSet
      //- on result (add created declarations)
      //- on withNameSet and on toNameSet
      etl = tom_make_TopDown(tom_make_GenResolveElement(this,resolveNameSet,result,withNameSet,toNameSet)).visitLight(elemTransfoList);
    } catch (VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.GenResolveElement: fail on " + transfo);
    }

    List bqlist = new LinkedList<BQTerm>();
    String strTransfoName = transfoName.getString();
    TomSymbol transfoSymbol = getSymbolTable().getSymbolFromName(strTransfoName);
    //elementary strategies generation
    //%match(elemTransfoList) {
    { /* unamed block */{ /* unamed block */if ( (etl instanceof tom.engine.adt.tomsignature.types.ElementaryTransformationList) ) {if ( (((( tom.engine.adt.tomsignature.types.ElementaryTransformationList )etl) instanceof tom.engine.adt.tomsignature.types.elementarytransformationlist.ConsconcElementaryTransformation) || ((( tom.engine.adt.tomsignature.types.ElementaryTransformationList )etl) instanceof tom.engine.adt.tomsignature.types.elementarytransformationlist.EmptyconcElementaryTransformation)) ) { tom.engine.adt.tomsignature.types.ElementaryTransformationList  tomMatch377_end_4=(( tom.engine.adt.tomsignature.types.ElementaryTransformationList )etl);do {{ /* unamed block */if (!( tomMatch377_end_4.isEmptyconcElementaryTransformation() )) { tom.engine.adt.tomsignature.types.ElementaryTransformation  tomMatch377_11= tomMatch377_end_4.getHeadconcElementaryTransformation() ;if ( ((( tom.engine.adt.tomsignature.types.ElementaryTransformation )tomMatch377_11) instanceof tom.engine.adt.tomsignature.types.elementarytransformation.ElementaryTransformation) ) { tom.engine.adt.tomname.types.TomName  tomMatch377_7= tomMatch377_11.getETName() ; tom.engine.adt.tomoption.types.OptionList  tomMatch377_10= tomMatch377_11.getOptions() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch377_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom___etName=tomMatch377_7; tom.engine.adt.code.types.BQTerm  tom___traversalStrat= tomMatch377_11.getTraversal() ;if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch377_10) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch377_10) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch377_end_19=tomMatch377_10;do {{ /* unamed block */if (!( tomMatch377_end_19.isEmptyconcOption() )) {if ( ((( tom.engine.adt.tomoption.types.Option ) tomMatch377_end_19.getHeadconcOption() ) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) { tom.engine.adt.tomoption.types.Option  tom___ot= tomMatch377_end_19.getHeadconcOption() ;

        //generate elementary `Strategy, `ReferenceClass and `SymbolDecl
        result.addAll(genElementaryStrategy(tom___etName,tom___traversalStrat, tomMatch377_11.getAstRuleInstructionList() ,transfoSymbol,tom___ot));
        //Generate symbol for elementary strategy and put it into symbol table
        TomSymbol astElemStratSymbol = generateElementaryStratSymbol(tom___ot,tom___etName,transfoDomain,transfoSymbol)
;
        getSymbolTable().putSymbol( tomMatch377_7.getString() ,astElemStratSymbol);
        //add the part of CompositeBQTerm transformer to bqlist
        bqlist.add(tom___traversalStrat);
      }}if ( tomMatch377_end_19.isEmptyconcOption() ) {tomMatch377_end_19=tomMatch377_10;} else {tomMatch377_end_19= tomMatch377_end_19.getTailconcOption() ;}}} while(!( (tomMatch377_end_19==tomMatch377_10) ));}}}}if ( tomMatch377_end_4.isEmptyconcElementaryTransformation() ) {tomMatch377_end_4=(( tom.engine.adt.tomsignature.types.ElementaryTransformationList )etl);} else {tomMatch377_end_4= tomMatch377_end_4.getTailconcElementaryTransformation() ;}}} while(!( (tomMatch377_end_4==(( tom.engine.adt.tomsignature.types.ElementaryTransformationList )etl)) ));}}}}

     

    if(resolveFlag) {

    //retrieve %transformation line number and current file name to forge
    //Resolve elements options
    int line = orgTrack.getLine();
    String fileName = orgTrack.getFileName();
    List<ResolveStratBlock> resolveStratBlockList = new LinkedList<ResolveStratBlock>();
    //build blocks for resolve strategy
    for(String toName:toNameSet) {
      List<ResolveStratElement> resolveStratElementList = new LinkedList<ResolveStratElement>();
      for(String withName:withNameSet) {
        String resolveStringName = TransformerPlugin.RESOLVE_ELEMENT_PREFIX+withName+toName;
        //do not create a ResolveStratElement if no corresponding resolve
        //element has been created / is necessary (from %resolve)
        if(getSymbolTable().getSymbolFromName(resolveStringName)!=null) {
          Option resolveOrgTrack =
             tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(resolveStringName) , line, fileName) ;
          resolveStratElementList.add( tom.engine.adt.tomsignature.types.resolvestratelement.ResolveStratElement.make(withName, resolveOrgTrack) );
        }
      }
      ResolveStratElementList astResolveStratElementList = ASTFactory.makeResolveStratElementList(resolveStratElementList);
      resolveStratBlockList.add( tom.engine.adt.tomsignature.types.resolvestratblock.ResolveStratBlock.make(toName, astResolveStratElementList) );
      //resolveStratElementList.clear();//no need to clear, we create a new one
    }

    ResolveStratBlockList rsblist =
      ASTFactory.makeResolveStratBlockList(resolveStratBlockList);


    TomNameList resolveNameList =  tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ;//populate in GenResolveElement strategy
    for(String stringName:resolveNameSet) {
      resolveNameList =  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(stringName) ,tom_append_list_concTomName(resolveNameList, tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() )) ;
    }

    //TODO: check fileFrom and fileTo
    try {
    fileFrom = checkAndNormalizeFileName(fileFrom, "source", orgTrack);
    fileTo = checkAndNormalizeFileName(fileTo, "target", orgTrack);
    } catch (Exception e) {
      e.printStackTrace();
    }
    //Generation of Resolve strategy
    Declaration resolveStratDecl = buildResolveStrat(transfoName,bqlist,transfoSymbol,rsblist,resolveNameList,fileFrom,fileTo,orgTrack)
;
    //add it to DeclarationList
    result.add(resolveStratDecl);

    }//end if(resolveFlag)

    //TODO: test hook
    //result.add(inverseLinks);

    //let's change the transformation MakeDecl
    //this first step is necessary due to the fact that an elementary
    //transformation may have many rules, therefore bqlist may have duplicate
    //bqterms
    BQTerm composite = makeComposedStrategy(bqlist,/*declList,*/transfoDomain);
    try {
      TomSymbol newSymbol =
        tom_make_TopDown(tom_make_ReplaceMakeDecl(composite)).visitLight(transfoSymbol);
      getSymbolTable().putSymbol(strTransfoName,newSymbol);
    } catch (VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.processSubDecl: fail on " + transfoSymbol);
    }
    return ASTFactory.makeDeclarationList(result);
  }//processSubDecl

  /**
   * Generate the `Strategy corresponding to an `ElementaryTransformation
   * @param strategyName the name of the strategy (elementary transformation)
   * @param traversal the traversal strategy which have been given to the
   * elementary transformation. It is used in the "transformer" strategy
   * composition.
   * @param riList the list of rules composing the elementary transformation
   * @param transfoSymbol the symbol of the transformation
   * @param orgTrack an option to track the construct
   * @return DeclarationList containclieng the `Strategy, the `SymbolDecl 
   * and a `ReferenceClass
   */
  private List<Declaration> genElementaryStrategy(TomName strategyName,
                                                  BQTerm traversal,
                                                  RuleInstructionList riList,
                                                  TomSymbol transfoSymbol,
                                                  Option orgTrack) {
    //build visitList
    List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
    List<RefClassTracelinkInstruction> refclassTInstructionList = new
      LinkedList<RefClassTracelinkInstruction>();
    TomType vType = null;
    List<Declaration> result = new LinkedList<Declaration>();
    { /* unamed block */{ /* unamed block */if ( (riList instanceof tom.engine.adt.tominstruction.types.RuleInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.RuleInstructionList )riList) instanceof tom.engine.adt.tominstruction.types.ruleinstructionlist.ConsconcRuleInstruction) || ((( tom.engine.adt.tominstruction.types.RuleInstructionList )riList) instanceof tom.engine.adt.tominstruction.types.ruleinstructionlist.EmptyconcRuleInstruction)) ) { tom.engine.adt.tominstruction.types.RuleInstructionList  tomMatch378_end_4=(( tom.engine.adt.tominstruction.types.RuleInstructionList )riList);do {{ /* unamed block */if (!( tomMatch378_end_4.isEmptyconcRuleInstruction() )) { tom.engine.adt.tominstruction.types.RuleInstruction  tomMatch378_11= tomMatch378_end_4.getHeadconcRuleInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.RuleInstruction )tomMatch378_11) instanceof tom.engine.adt.tominstruction.types.ruleinstruction.RuleInstruction) ) { tom.engine.adt.tomterm.types.TomTerm  tom___term= tomMatch378_11.getTerm() ; tom.engine.adt.tominstruction.types.InstructionList  tom___instr= tomMatch378_11.getAction() ;

        List<TomName> nameList = new LinkedList<TomName>();
        //collect Tracelink instructions in order to build the ReferenceClass
        { /* unamed block */{ /* unamed block */if ( (tom___instr instanceof tom.engine.adt.tominstruction.types.InstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tom___instr) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tom___instr) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch379_end_4=(( tom.engine.adt.tominstruction.types.InstructionList )tom___instr);do {{ /* unamed block */if (!( tomMatch379_end_4.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch379_9= tomMatch379_end_4.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch379_9) instanceof tom.engine.adt.tominstruction.types.instruction.Tracelink) ) { tom.engine.adt.tomname.types.TomName  tom___n= tomMatch379_9.getName() ;

            refclassTInstructionList.add( tom.engine.adt.tominstruction.types.refclasstracelinkinstruction.RefClassTracelinkInstruction.make( tomMatch379_9.getType() , tom___n) );
            nameList.add(tom___n);
          }}if ( tomMatch379_end_4.isEmptyconcInstruction() ) {tomMatch379_end_4=(( tom.engine.adt.tominstruction.types.InstructionList )tom___instr);} else {tomMatch379_end_4= tomMatch379_end_4.getTailconcInstruction() ;}}} while(!( (tomMatch379_end_4==(( tom.engine.adt.tominstruction.types.InstructionList )tom___instr)) ));}}}}

        //links to be set
        TomNameList tracedLinks = ASTFactory.makeNameList(nameList);
        BQTerm current = genTracelinkPopulateResolveCurrent(tom___term);
        /*cases:
        !tracelink ^  resolve => err
        !tracelink ^ !resolve => OK, but no need to execute this part of code
         tracelink ^  resolve => OK
         tracelink ^ !resolve => OK
        */
        InstructionList instrList =  tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ;
        if(current==null) {
          if(resolveFlag) {//if there is a resolve while no tracelink: err
            throw new TomRuntimeException("TransformerPlugin.process: fail to generate tracelink backquote term corresponding to "+tom___term);
          }
          //in case there is no tracelink
          instrList = tom_append_list_concInstruction(tom___instr, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() );
        } else {//in case there is at least one tracelink
          TomName firstArgument = TomBase.getSlotName(transfoSymbol,0);
          TomType firstArgType = TomBase.getSlotType(transfoSymbol,
                                                     firstArgument);
          BQTerm link =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , firstArgument, firstArgType) ;
          TomName refClassName =
             tom.engine.adt.tomname.types.tomname.Name.make(TransformerPlugin.REFCLASS_PREFIX+strategyName.getString()) ;
          Instruction tracelinkPopResolveInstruction =
             tom.engine.adt.tominstruction.types.instruction.TracelinkPopulateResolve.make(refClassName, tracedLinks, current, link) ;
          instrList = tom_append_list_concInstruction(tom___instr, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tracelinkPopResolveInstruction, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) );
          RefClassTracelinkInstructionList refclassInstructions = ASTFactory.makeRefClassTracelinkInstructionList(refclassTInstructionList);
        //TODO: use Class(AstName:TomName,ExtendsType:TomType,SuperTerm:BQTerm,Declaration:Declaration)
        Declaration refClass =  tom.engine.adt.tomdeclaration.types.declaration.ReferenceClass.make(refClassName, refclassInstructions) ;
        result.add(refClass);
        }//case tracelink
        vType =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,  tomMatch378_11.getTypeName() ,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
        //add a test on vType here: if(vType!=null && vType!=)
        BQTerm subject =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("tom__arg") , vType) 

;
        Constraint constraint =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom___term, subject, vType) 
                                               , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) 



;
        ciList.add( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(constraint,  tom.engine.adt.tominstruction.types.instruction.RawAction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.TrueTL.make() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instrList) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ) ,  tomMatch378_11.getOptions() ) 






                );
      }}if ( tomMatch378_end_4.isEmptyconcRuleInstruction() ) {tomMatch378_end_4=(( tom.engine.adt.tominstruction.types.RuleInstructionList )riList);} else {tomMatch378_end_4= tomMatch378_end_4.getTailconcRuleInstruction() ;}}} while(!( (tomMatch378_end_4==(( tom.engine.adt.tominstruction.types.RuleInstructionList )riList)) ));}}}}

    ConstraintInstructionList astCiList =
      ASTFactory.makeConstraintInstructionList(ciList);
    //build the strategy (elementary transformation)
    LinkedList<Option> list = new LinkedList<Option>();
    list.add(orgTrack);
    OptionList options = ASTFactory.makeOptionList(list);
    List<TomVisit> visitList = new LinkedList<TomVisit>();
    visitList.add( tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make(vType, astCiList, options) );
    TomVisitList astVisitList = ASTFactory.makeTomVisitList(visitList);

    BQTerm extendsTerm =  tom.engine.adt.code.types.bqterm.BQAppl.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make(strategyName, orgTrack.getLine(), orgTrack.getFileName()) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("Identity") ,  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) 


;
    Declaration strategy =  tom.engine.adt.tomdeclaration.types.declaration.Strategy.make(strategyName, extendsTerm, astVisitList,  tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() , orgTrack) 



;
    //add forged strategy + symbol to the declarations list
    result.add(strategy);
    result.add( tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl.make(strategyName) );
    return result;
  }//genElementaryStrategy

  /**
   *
   * @param term the term on which the tracelink BQTerm is based
   * @return the Backquote term representing 
   */
  //TODO: change this, to specific, NOK with complex patterns
  private BQTerm genTracelinkPopulateResolveCurrent(TomTerm term) {
    BQTerm result = null;
    { /* unamed block */{ /* unamed block */if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch380_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getConstraints() ;if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tomMatch380_1) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tomMatch380_1) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) { tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch380_end_7=tomMatch380_1;do {{ /* unamed block */if (!( tomMatch380_end_7.isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch380_11= tomMatch380_end_7.getHeadconcConstraint() ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch380_11) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom___t= tomMatch380_11.getVar() ;{ /* unamed block */{ /* unamed block */if ( (tom___t instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom___t) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {



            result =  tom.engine.adt.code.types.bqterm.BQVariable.make( (( tom.engine.adt.tomterm.types.TomTerm )tom___t).getOptions() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom___t).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom___t).getAstType() ) ;
          }}}}

      }}if ( tomMatch380_end_7.isEmptyconcConstraint() ) {tomMatch380_end_7=tomMatch380_1;} else {tomMatch380_end_7= tomMatch380_end_7.getTailconcConstraint() ;}}} while(!( (tomMatch380_end_7==tomMatch380_1) ));}}}}}

    return result;
  }

  /**
   * Generate TomSymbol for each strategy representing an elementary
   * transformation
   * @param orgTrack an option to track the construct
   * @param stratName the name of the strategy
   * @param transfoDomain the domain of the strategy (the same as the
   * transformation domain)
   * @param symbol the transformation symbol where parameters are retrieved
   * @return The generated symbol of an elementary strategy
   */
  private TomSymbol generateElementaryStratSymbol(Option orgTrack,
                                                  TomName stratName,
                                                  TomTypeList transfoDomain,
                                                  TomSymbol transfoSymbol) {
    List<Option> optionList = new LinkedList<Option>();
    optionList.add(orgTrack);
    
    TomType strategyType =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , "Strategy",  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
    BQTermList makeArgs =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    BQTermList params =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    //parameters
    String stringStratName = stratName.getString();
    String makeTlCode = "new "+stringStratName+"(";
    int index = 0;
    TomTypeList makeTypes = transfoDomain; //`concTomType();
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      if (index>0) {//if many parameters
        makeTlCode = makeTlCode.concat(",");
      }
      makeTlCode += argName;

      BQTerm arg =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(argName) , makeTypes.getHeadconcTomType()) ;
      makeArgs = tom_append_list_concBQTerm(makeArgs, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
      params = tom_append_list_concBQTerm(params, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(arg) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );//build part of CompositeBQTerm
      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }
    makeTlCode += ")";
    Option makeOption =  tom.engine.adt.tomoption.types.option.OriginTracking.make(stratName, orgTrack.getLine(), orgTrack.getFileName()) ;
    Declaration makeDecl =  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make(stratName, strategyType, makeArgs,  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make(makeTlCode) ) ) , makeOption) 
;
    optionList.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(makeDecl) );

    Option fsymOption =  tom.engine.adt.tomoption.types.option.OriginTracking.make(stratName, orgTrack.getLine(), orgTrack.getFileName()) ;
    String varname = "t";
    BQTerm fsymVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(fsymOption, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make(varname) , strategyType) ;
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringStratName+")",varname);
    Declaration fsymDecl =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make(stratName, fsymVar,  tom.engine.adt.tomexpression.types.expression.Code.make(code) , fsymOption) ;
    optionList.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(fsymDecl) );

    //TomTypeList transfoDomain = TomBase.getSymbolDomain(transfoSymbol);
    PairNameDeclList paramDecl =
      genStratPairNameDeclListFromTransfoSymbol(stratName,transfoSymbol);
    return ASTFactory.makeSymbol(stringStratName, strategyType, transfoDomain,
        paramDecl, optionList);
  }//generateElementaryStratSymbol


  /**
   * This strategy modifies the MakeDecl of a transformation symbol by adding
   * a forged BQTerm
   */
  public static class ReplaceMakeDecl extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.code.types.BQTerm  composite;public ReplaceMakeDecl( tom.engine.adt.code.types.BQTerm  composite) {super(( new tom.library.sl.Identity() ));this.composite=composite;}public  tom.engine.adt.code.types.BQTerm  getcomposite() {return composite;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {


        return  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make( (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstType() ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getArgs() ,  tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction.make(composite) ,  (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOrgTrack() ) ;
      }}}}return _visit_Declaration(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ReplaceMakeDecl( tom.engine.adt.code.types.BQTerm  t0) { return new ReplaceMakeDecl(t0);}



  /**
   * This function generates the Backquote term of the transformation
   * @param bqlist the list of BQTerm representing the elementary
   * transformation strategies, and composing the whole transformation
   * @param declList a declaration list necessary to build the Resolve BQTerm
   * @param transfoDomain the domain of the transformation, used to build the
   * Resolve BQTerm
   * @return a Backquote term representing the whole transformation strategy
   */
  private BQTerm makeComposedStrategy(List<BQTerm> bqlist, 
                                      /*DeclarationList declList, */
                                      TomTypeList transfoDomain) {
    BQTermList bql = removeDuplicate(ASTFactory.makeBQTermList(bqlist));
    /*concOption() for the moment, to change*/
    BQTerm transfos =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.BQAppl.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("Sequence") , bql) 
          ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) 



;
    //TODO
    //add condition: call it only if resolve is needed
    /*BQTermList res = `concBQTerm(transfos, 
                                 makeResolveBQTerm(declList, transfoDomain));
    BQTerm transformerBQTerm = `Composite(CompositeBQTerm(BQAppl(
            concOption(),
            Name("Sequence"),
            res)
          ));
    return transformerBQTerm;*/
    return transfos;
  }

  /**
   * This function generates the Resolve BQTerm used in the whole
   * transformation BQTerm
   * @param declList the declaration list containing ResolveStratDecl() nodes
   * @param transfoDomain the domain of the transformation
   * @return the generated Resolve BQTerm
   */
  private BQTerm makeResolveBQTerm(DeclarationList declList, 
                                   TomTypeList transfoDomain) {
    Option option =  tom.engine.adt.tomoption.types.option.noOption.make() ;
    String stringRSname = "";
    { /* unamed block */{ /* unamed block */if ( (declList instanceof tom.engine.adt.tomdeclaration.types.DeclarationList) ) {if ( (((( tom.engine.adt.tomdeclaration.types.DeclarationList )declList) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration) || ((( tom.engine.adt.tomdeclaration.types.DeclarationList )declList) instanceof tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration)) ) { tom.engine.adt.tomdeclaration.types.DeclarationList  tomMatch383_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )declList);do {{ /* unamed block */if (!( tomMatch383_end_4.isEmptyconcDeclaration() )) { tom.engine.adt.tomdeclaration.types.Declaration  tomMatch383_9= tomMatch383_end_4.getHeadconcDeclaration() ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch383_9) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveStratDecl) ) {

        option =  tomMatch383_9.getOriginTracking() ;
        stringRSname = TransformerPlugin.STRAT_RESOLVE_PREFIX+ tomMatch383_9.getTransfoName() ;
      }}if ( tomMatch383_end_4.isEmptyconcDeclaration() ) {tomMatch383_end_4=(( tom.engine.adt.tomdeclaration.types.DeclarationList )declList);} else {tomMatch383_end_4= tomMatch383_end_4.getTailconcDeclaration() ;}}} while(!( (tomMatch383_end_4==(( tom.engine.adt.tomdeclaration.types.DeclarationList )declList)) ));}}}}

    //build parameters
    BQTermList params =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    int index = 0;
    TomTypeList makeTypes = transfoDomain;
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      BQTerm arg =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(argName) , makeTypes.getHeadconcTomType()) ;
      params = tom_append_list_concBQTerm(params, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(arg) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }
    //build resolve part of the BQComposite
    //TODO?: change that to avoid to have a hardcoded default traversal?
    BQTerm bqtrans =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.BQAppl.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(option, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("TopDown") ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.BQAppl.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(option, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make(stringRSname) , params) 
                  ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) 



;
    return bqtrans;
  }

  /**
   * This function compares (partly) two backquote terms. This "equality" is
   * only based on the AstName contained in the OriginTracking option.
   * @param bqt1 a backquote term
   * @param bqt2 a backquote term to compare to the first one
   * @return the result of the "test of equality"
   */
  private boolean partialEqualsBQTerm(BQTerm bqt1, BQTerm bqt2) {
    { /* unamed block */{ /* unamed block */if ( (bqt1 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )bqt1) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )bqt1) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {if (!( (( tom.engine.adt.code.types.BQTerm )bqt1).isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tomMatch384_7= (( tom.engine.adt.code.types.BQTerm )bqt1).getHeadComposite() ;if ( ((( tom.engine.adt.code.types.CompositeMember )tomMatch384_7) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) { tom.engine.adt.code.types.BQTerm  tomMatch384_6= tomMatch384_7.getterm() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch384_6) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch384_9= tomMatch384_6.getOptions() ;if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch384_9) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch384_9) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch384_end_21=tomMatch384_9;do {{ /* unamed block */if (!( tomMatch384_end_21.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch384_31= tomMatch384_end_21.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch384_31) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {if (  (( tom.engine.adt.code.types.BQTerm )bqt1).getTailComposite() .isEmptyComposite() ) {if ( (bqt2 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )bqt2) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )bqt2) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {if (!( (( tom.engine.adt.code.types.BQTerm )bqt2).isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tomMatch384_13= (( tom.engine.adt.code.types.BQTerm )bqt2).getHeadComposite() ;if ( ((( tom.engine.adt.code.types.CompositeMember )tomMatch384_13) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) { tom.engine.adt.code.types.BQTerm  tomMatch384_12= tomMatch384_13.getterm() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch384_12) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch384_15= tomMatch384_12.getOptions() ;if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch384_15) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch384_15) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch384_end_27=tomMatch384_15;do {{ /* unamed block */if (!( tomMatch384_end_27.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch384_34= tomMatch384_end_27.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch384_34) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {if ( ( tomMatch384_31.getAstName() == tomMatch384_34.getAstName() ) ) {if (  (( tom.engine.adt.code.types.BQTerm )bqt2).getTailComposite() .isEmptyComposite() ) {


 return true; }}}}if ( tomMatch384_end_27.isEmptyconcOption() ) {tomMatch384_end_27=tomMatch384_15;} else {tomMatch384_end_27= tomMatch384_end_27.getTailconcOption() ;}}} while(!( (tomMatch384_end_27==tomMatch384_15) ));}}}}}}}}}if ( tomMatch384_end_21.isEmptyconcOption() ) {tomMatch384_end_21=tomMatch384_9;} else {tomMatch384_end_21= tomMatch384_end_21.getTailconcOption() ;}}} while(!( (tomMatch384_end_21==tomMatch384_9) ));}}}}}}}}

    return false;
  }

  /**
   * This function removes duplicate BQTerm from a BQTermList
   * @param bqlist a BQTermList which may contain duplicate BQTerm
   * @return a BQTermList without any duplicate BQTerm
   */
  private BQTermList removeDuplicate(BQTermList bqlist) {
    BQTermList result =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    for(BQTerm bqterm : bqlist.getCollectionconcBQTerm()) {
      boolean duplicate = false;
      for(BQTerm bqt : result.getCollectionconcBQTerm()) {
        duplicate = partialEqualsBQTerm(bqterm, bqt);
        if(duplicate) {
          break;
        }
      }
      if(!duplicate) {
        result = tom_append_list_concBQTerm(result, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(bqterm, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
      }
    }
    return result;
  }



  /**
   * Generates the Resolve strategy
   * Here is an example of code for this strategy:
   * %strategy Resolve(tom__linkClass:LinkClass,model:PetriNet) extends Identity() {
   *   visit Place {
   *     tom__arg@ResolveWorkDefinitionPlace[o=o,name=name] -> {
   *       Place res = (Place) tom__linkClass.get(`o).get(`name);
   *       `resolveInverseLinks(tom__arg, res, model);
   *       return res;
   *     }
   *   }
   * 
   *   visit Transition {
   *     tom__arg@ResolveWorkDefinitionTransition[o=o,name=name] -> {
   *       Transition res = (Transition) tom__linkClass.get(`o).get(`name);
   *       `resolveInverseLinks(tom__arg, res, model);
   *       return res;
   *     }
   *     tom__arg@ResolveProcessTransition[o=o,name=name] -> {
   *       Transition res = (Transition) tom__linkClass.get(`o).get(`name);
   *       `resolveInverseLinks(tom__arg, res, model);
   *       return res;
   *     }
   *   }
   * }
   * @param transfoName the name of the transformation
   * @param bqlist
   * @param transformationSymbol
   * @param rsbList
   * @param resolveNameList
   * @param fileFrom the source metamodel file name
   * @param fileTo the target metamodel file name
   * @param ot 
   * @return the AbstractDecl containing the Resolve Strategy and its
   * SymbolDecl
   */
  private Declaration buildResolveStrat(TomName transfoName,
                                        List bqlist, 
                                        TomSymbol transformationSymbol, 
                                        ResolveStratBlockList rsbList, 
                                        TomNameList resolveNameList,
                                        String fileFrom,
                                        String fileTo,
                                        Option ot) {
    List<TomVisit> visitList = new LinkedList<TomVisit>();

    { /* unamed block */{ /* unamed block */if ( (rsbList instanceof tom.engine.adt.tomsignature.types.ResolveStratBlockList) ) {if ( (((( tom.engine.adt.tomsignature.types.ResolveStratBlockList )rsbList) instanceof tom.engine.adt.tomsignature.types.resolvestratblocklist.ConsconcResolveStratBlock) || ((( tom.engine.adt.tomsignature.types.ResolveStratBlockList )rsbList) instanceof tom.engine.adt.tomsignature.types.resolvestratblocklist.EmptyconcResolveStratBlock)) ) { tom.engine.adt.tomsignature.types.ResolveStratBlockList  tomMatch385_end_4=(( tom.engine.adt.tomsignature.types.ResolveStratBlockList )rsbList);do {{ /* unamed block */if (!( tomMatch385_end_4.isEmptyconcResolveStratBlock() )) { tom.engine.adt.tomsignature.types.ResolveStratBlock  tomMatch385_9= tomMatch385_end_4.getHeadconcResolveStratBlock() ;if ( ((( tom.engine.adt.tomsignature.types.ResolveStratBlock )tomMatch385_9) instanceof tom.engine.adt.tomsignature.types.resolvestratblock.ResolveStratBlock) ) { String  tom___tname= tomMatch385_9.getToName() ; tom.engine.adt.tomsignature.types.ResolveStratElementList  tom___rseList= tomMatch385_9.getresolveStratElementList() ;

        TomType ttype =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , tom___tname,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
        //TODO: to check
        List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
        { /* unamed block */{ /* unamed block */if ( (tom___rseList instanceof tom.engine.adt.tomsignature.types.ResolveStratElementList) ) {if ( (((( tom.engine.adt.tomsignature.types.ResolveStratElementList )tom___rseList) instanceof tom.engine.adt.tomsignature.types.resolvestratelementlist.ConsconcResolveStratElement) || ((( tom.engine.adt.tomsignature.types.ResolveStratElementList )tom___rseList) instanceof tom.engine.adt.tomsignature.types.resolvestratelementlist.EmptyconcResolveStratElement)) ) { tom.engine.adt.tomsignature.types.ResolveStratElementList  tomMatch386_end_4=(( tom.engine.adt.tomsignature.types.ResolveStratElementList )tom___rseList);do {{ /* unamed block */if (!( tomMatch386_end_4.isEmptyconcResolveStratElement() )) { tom.engine.adt.tomsignature.types.ResolveStratElement  tomMatch386_9= tomMatch386_end_4.getHeadconcResolveStratElement() ;if ( ((( tom.engine.adt.tomsignature.types.ResolveStratElement )tomMatch386_9) instanceof tom.engine.adt.tomsignature.types.resolvestratelement.ResolveStratElement) ) { tom.engine.adt.tomoption.types.Option  tom___rot= tomMatch386_9.getResolveOrgTrack() ;


            int rline = tom___rot.getLine();
            String rfileName = tom___rot.getFileName();
            Slot sloto = genPairSlotAppl(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_O, rline, rfileName);
            Slot slotname = genPairSlotAppl(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_NAME, rline, rfileName);
            
            TomTerm pattern =  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___rot, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(tom___rot.getAstName(), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ,  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(sloto, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(slotname, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) ) ,  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( tom.engine.adt.tomconstraint.types.constraint.AliasTo.make( tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("tom__resolve") , tom___rot.getLine(), tom___rot.getFileName()) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("tom__resolve") , getSymbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() 
                        ) 
                      ) 
                    , tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) 
                  ) 
















;
            //getSymbolTable().TYPE_UNKNOWN <->
            //Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),

            //maybe something like that in the future
            //TomType matchType = (getOptionBooleanValue("newtyper")?SymbolTable.TYPE_UNKNOWN:ttype);
            BQTerm subject =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("tom__arg") , ttype) ;
            Constraint constraint =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(pattern, subject, ttype) , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) 
;

            TomName firstArgument = TomBase.getSlotName(transformationSymbol,0);
            TomName secondArgument = TomBase.getSlotName(transformationSymbol,1);
            TomType firstArgType = TomBase.getSlotType(transformationSymbol,firstArgument);
            BQTerm link =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , firstArgument, firstArgType) ;
            BQTerm res =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("res") , ttype) ;
            BQTerm first =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_O) , SymbolTable.TYPE_UNKNOWN) ;
            BQTerm second =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_NAME) , SymbolTable.TYPE_UNKNOWN) ;
            //replace ITL() by FunctionCall()?
            //to change: language specific
            //Instruction referenceStatement = `LetRef(res,
            Instruction referenceStatement =  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make(TomBase.getTLType(getSymbolTable().getType(tom___tname))) ) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Assign.make(res,  tom.engine.adt.tomexpression.types.expression.Cast.make(ttype,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(link) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make(".get(") ) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(first) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make(").get(") ) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(second) , tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make(")") ) 
                          , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ) ) ) ) ) ) 
                    ) 
                  , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 











;

            BQTerm model =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , secondArgument, SymbolTable.TYPE_UNKNOWN) ;
            Instruction resolveStatement =  tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make(TransformerPlugin.RESOLVE_INVERSE_LINK_FUNCTION) , getSymbolTable().getVoidType(),  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(subject, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(res, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(model, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ) ) 

;
            Instruction returnStatement =  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("res") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ) ;
            /*referenceStatement,*/ 
            InstructionList instructions =  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(referenceStatement, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(resolveStatement, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make(";") ) ) , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(returnStatement, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ;
            ciList.add( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(constraint,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(instructions) ,  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(tom___rot, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ) );
          }}if ( tomMatch386_end_4.isEmptyconcResolveStratElement() ) {tomMatch386_end_4=(( tom.engine.adt.tomsignature.types.ResolveStratElementList )tom___rseList);} else {tomMatch386_end_4= tomMatch386_end_4.getTailconcResolveStratElement() ;}}} while(!( (tomMatch386_end_4==(( tom.engine.adt.tomsignature.types.ResolveStratElementList )tom___rseList)) ));}}}}


        visitList.add( tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make(ttype, ASTFactory.makeConstraintInstructionList(ciList),  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(ot, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ) 

);
        ciList.clear(); //reset ConstraintInstructionList
      }}if ( tomMatch385_end_4.isEmptyconcResolveStratBlock() ) {tomMatch385_end_4=(( tom.engine.adt.tomsignature.types.ResolveStratBlockList )rsbList);} else {tomMatch385_end_4= tomMatch385_end_4.getTailconcResolveStratBlock() ;}}} while(!( (tomMatch385_end_4==(( tom.engine.adt.tomsignature.types.ResolveStratBlockList )rsbList)) ));}}}}

    String stringRSname = TransformerPlugin.STRAT_RESOLVE_PREFIX+transfoName.getString();
    TomName rsname =  tom.engine.adt.tomname.types.tomname.Name.make(stringRSname) ;
    BQTerm extendsTerm =  tom.engine.adt.code.types.bqterm.BQAppl.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(ot, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("Identity") ,  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ;
    TomVisitList astVisitList = ASTFactory.makeTomVisitList(visitList);
    int line = ot.getLine();
    String fileName = ot.getFileName();
    Option orgTrack =  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("Strategy") , line, fileName) ;
    Declaration inverseLinks =  tom.engine.adt.tomdeclaration.types.declaration.ResolveInverseLinksDecl.make(resolveNameList, fileFrom, fileTo) ;

    Declaration resolve =  tom.engine.adt.tomdeclaration.types.declaration.Strategy.make(rsname, extendsTerm, astVisitList,  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(inverseLinks, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) , orgTrack) 
;

    List<Option> options = new LinkedList<Option>();
    options.add(ot);
    //definition of make.
    //code generation of IsFsymDecl
    TomType strategyType =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , "Strategy",  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
    /* Arguments of the strategy. For the moment, nothing, but it will be
       necessary */
    BQTermList makeArgs =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    BQTermList params =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    //parameters
    //factorize this code with other buildStrat functions?
    String makeTlCode = "new "+stringRSname+"(";
    int index = 0;
    TomTypeList transfoDomain = TomBase.getSymbolDomain(transformationSymbol);
    TomTypeList makeTypes = transfoDomain; //`concTomType();
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      if (index>0) {//if many parameters
        makeTlCode = makeTlCode.concat(",");
      }
      makeTlCode += argName;

      BQTerm arg =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(argName) , makeTypes.getHeadconcTomType()) ;
      makeArgs = tom_append_list_concBQTerm(makeArgs, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
      params = tom_append_list_concBQTerm(params, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(arg) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );//build part of CompositeBQTerm

      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }
    makeTlCode += ")";
    //should not be ot, since the line number is different, but here, we use ot
    Option makeOption =  tom.engine.adt.tomoption.types.option.OriginTracking.make(rsname, line, fileName) ;
    
    Declaration makeDecl =  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make(rsname, strategyType, makeArgs,  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.ITL.make(makeTlCode) ) ) , makeOption) 
;
    options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(makeDecl) );

    //definition of the is_fsym method.
    //should not be ot, since the line number is different, but here, we use ot
    Option fsymOption =  tom.engine.adt.tomoption.types.option.OriginTracking.make(rsname, line, fileName) ;
    String varname = "t";
    BQTerm fsymVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(fsymOption, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make(varname) , strategyType) ;
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringRSname+")",varname);
    Declaration fsymDecl =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make(rsname, fsymVar,  tom.engine.adt.tomexpression.types.expression.Code.make(code) , fsymOption) ;
    options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(fsymDecl) );

    //TomType transfoCodomain = TomBase.getSymbolCodomain(transformationSymbol);
    //types <-> `concTomType() (no param)
    //slots <-> `concPairNameDecl() (no param)
    //PairNameDeclList paramDecl = transformationSymbol.getPairNameDeclList();
    PairNameDeclList paramDecl =
      genStratPairNameDeclListFromTransfoSymbol(rsname,transformationSymbol);
    TomSymbol astSymbol = ASTFactory.makeSymbol(stringRSname, strategyType,
        transfoDomain, paramDecl, options);
    getSymbolTable().putSymbol(stringRSname,astSymbol);

    //build here the part of BQTermComposite transformer
/*    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(ot), Name("TopDown"), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(ot),Name(stringRSname),params)
                  ))))));
    bqlist.add(bqtrans);*/
    //result.add(`AbstractDecl(concDeclaration(resolve,SymbolDecl(rsname))));
    return  tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl.make( tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(resolve, tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl.make(rsname) , tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) ) ) ;
  } //buildResolveStrat

  /**
   * Generates and returns the list of the strategy parameters
   * @param stratName the name of the strategy
   * @param transformationSymbol the symbol of the transformation, needed to
   * retrieve parameters
   * @return the PairNameDeclList containing the strategy parameters
   */
  private static PairNameDeclList genStratPairNameDeclListFromTransfoSymbol(TomName stratName, 
                                                                            TomSymbol transformationSymbol) {
    PairNameDeclList result =  tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ;
    PairNameDeclList transformationParameters = transformationSymbol.getPairNameDeclList();
    { /* unamed block */{ /* unamed block */if ( (transformationParameters instanceof tom.engine.adt.tomslot.types.PairNameDeclList) ) {if ( (((( tom.engine.adt.tomslot.types.PairNameDeclList )transformationParameters) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl) || ((( tom.engine.adt.tomslot.types.PairNameDeclList )transformationParameters) instanceof tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl)) ) { tom.engine.adt.tomslot.types.PairNameDeclList  tomMatch387_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )transformationParameters);do {{ /* unamed block */if (!( tomMatch387_end_4.isEmptyconcPairNameDecl() )) { tom.engine.adt.tomslot.types.PairNameDecl  tomMatch387_9= tomMatch387_end_4.getHeadconcPairNameDecl() ;if ( ((( tom.engine.adt.tomslot.types.PairNameDecl )tomMatch387_9) instanceof tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl) ) { tom.engine.adt.tomdeclaration.types.Declaration  tomMatch387_8= tomMatch387_9.getSlotDecl() ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch387_8) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) {


        PairNameDecl pnd =  tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make( tomMatch387_9.getSlotName() ,  tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl.make(stratName,  tomMatch387_8.getSlotName() ,  tomMatch387_8.getVariable() ,  tomMatch387_8.getExpr() ,  tomMatch387_8.getOrgTrack() ) ) ;
        result = tom_append_list_concPairNameDecl(result, tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make(pnd, tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) );
      }}}if ( tomMatch387_end_4.isEmptyconcPairNameDecl() ) {tomMatch387_end_4=(( tom.engine.adt.tomslot.types.PairNameDeclList )transformationParameters);} else {tomMatch387_end_4= tomMatch387_end_4.getTailconcPairNameDecl() ;}}} while(!( (tomMatch387_end_4==(( tom.engine.adt.tomslot.types.PairNameDeclList )transformationParameters)) ));}}}}

    return result;
  }

  /**
   * Generates a PairSlotAppl with an unknown type
   * @param name the name of the slot
   * @param line the line number for origin tracking
   * @param fileName the fileName for origin tracking
   * @return the generated PairSlotAppl
   */
  private Slot genPairSlotAppl(String name, int line, String fileName) {
    return genPairSlotAppl(name,line,fileName,getSymbolTable().TYPE_UNKNOWN);
  }

   /**
   * Generates a PairSlotAppl with a known type
   * @param name the name of the slot
   * @param line the line number for origin tracking
   * @param fileName the fileName for origin tracking
   * @param type the type of the Slot
   * @return the generated PairSlotAppl
   */
  private Slot genPairSlotAppl(String name, int line, String fileName, TomType type) {
    return  tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(name) ,  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(name) , line, fileName) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make(name) , type,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() 
          ) 
        ) 







;
  }


  public static class GenResolveElement extends tom.library.sl.AbstractStrategyBasic {private  TransformerPlugin  transformer;private  java.util.HashSet  resolveNameSet;private  java.util.List  result;private  java.util.HashSet  withNameSet;private  java.util.HashSet  toNameSet;public GenResolveElement( TransformerPlugin  transformer,  java.util.HashSet  resolveNameSet,  java.util.List  result,  java.util.HashSet  withNameSet,  java.util.HashSet  toNameSet) {super(( new tom.library.sl.Identity() ));this.transformer=transformer;this.resolveNameSet=resolveNameSet;this.result=result;this.withNameSet=withNameSet;this.toNameSet=toNameSet;}public  TransformerPlugin  gettransformer() {return transformer;}public  java.util.HashSet  getresolveNameSet() {return resolveNameSet;}public  java.util.List  getresult() {return result;}public  java.util.HashSet  getwithNameSet() {return withNameSet;}public  java.util.HashSet  gettoNameSet() {return toNameSet;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Resolve) ) { String  tom___sname= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSrc() ; String  tom___stypename= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSType() ; String  tom___ttypename= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getTType() ; tom.engine.adt.tomoption.types.Option  tom___ot= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOrgTrack() ;


        //try to say that a resolve node has been found
        transformer.resolveFlag = true;

        String resolveStringName = transformer.RESOLVE_ELEMENT_PREFIX+tom___stypename+tom___ttypename;
        int line = tom___ot.getLine();
        String fileName = tom___ot.getFileName();
if(!resolveNameSet.contains(resolveStringName)) {
        resolveNameSet.add(resolveStringName);
        //used for generation of ResolveStratElement and ResolveStratBlock
        withNameSet.add(tom___stypename);
        toNameSet.add(tom___ttypename);

        DeclarationList resolveTTDecl=  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl.make( tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("t") , line, fileName) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("t") ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , resolveStringName,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ) ,  tom.engine.adt.tomexpression.types.expression.Code.make(resolveStringName) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("is_sort") , line, fileName) 
              ) 
            , tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) 








;

        TomName resolveName =  tom.engine.adt.tomname.types.tomname.Name.make(resolveStringName) ;
        Option resolveOrgTrack =  tom.engine.adt.tomoption.types.option.OriginTracking.make(resolveName, line, fileName) ;
        TomType stype =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , tom___stypename,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
        //  /!\ Java dependant: ("String")
        TomType stringType =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , "String",  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
        TomTypeList rtypes =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(stype, tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(stringType, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) ) ;
        TomType tType =  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , tom___ttypename,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;

        //generate PairNameDecl for parameters of Resolve elements
        PairNameDecl objectPNDecl = transformer.genResolvePairNameDecl(transformer.RESOLVE_ELEMENT_ATTRIBUTE_O, resolveStringName, line, fileName, tType);
        PairNameDecl namePNDecl = transformer.genResolvePairNameDecl(transformer.RESOLVE_ELEMENT_ATTRIBUTE_NAME, resolveStringName, line, fileName, tType);

        List<PairNameDecl> resolvePairNameDeclList = new LinkedList<PairNameDecl>();
        resolvePairNameDeclList.add(objectPNDecl);
        resolvePairNameDeclList.add(namePNDecl);

        Declaration isfsymDecl =  tom.engine.adt.tomdeclaration.types.declaration.ResolveIsFsymDecl.make(resolveName,  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("t") , line, fileName) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("t") , tType) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("is_fsym") , line, fileName) 
) 



;

        BQTermList makeArgs =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(transformer.RESOLVE_ELEMENT_ATTRIBUTE_O) , line, fileName) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make(transformer.RESOLVE_ELEMENT_ATTRIBUTE_O) , stype) , tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(transformer.RESOLVE_ELEMENT_ATTRIBUTE_NAME) , line, fileName) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make(transformer.RESOLVE_ELEMENT_ATTRIBUTE_NAME) , stringType) 
            , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) 


;

        Declaration makeDecl =  tom.engine.adt.tomdeclaration.types.declaration.ResolveMakeDecl.make(resolveName, tType, makeArgs,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("make") , line, fileName) 
            ) 




;

        List<Option> symbolOptions = new LinkedList<Option>();
        symbolOptions.add(tom___ot);
        symbolOptions.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(makeDecl) );
        symbolOptions.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(isfsymDecl) );

        //create Resolve constructor symbol ("ResolveOp")
        TomSymbol astSymbol = ASTFactory.makeSymbol(
            resolveStringName,
            tType,
            rtypes,
            ASTFactory.makePairNameDeclList(resolvePairNameDeclList),
            symbolOptions
            );
        //add "ResolveOp" symbol into SymbolTable
        transformer.getSymbolTable().putSymbol(resolveStringName,astSymbol);

        result.add( tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl.make(resolveName, resolveTTDecl, resolveOrgTrack) );
        result.add( tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(resolveStringName) ) );

        String extendsName = null;
        TomSymbol symb = transformer.getSymbolTable().getSymbolFromName(tom___ttypename);
        //retrieve classimpl
        OptionList ol = symb.getOptions();
        { /* unamed block */{ /* unamed block */if ( (ol instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )ol) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )ol) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch389_end_4=(( tom.engine.adt.tomoption.types.OptionList )ol);do {{ /* unamed block */if (!( tomMatch389_end_4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch389_8= tomMatch389_end_4.getHeadconcOption() ;if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch389_8) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) { tom.engine.adt.tomdeclaration.types.Declaration  tomMatch389_7= tomMatch389_8.getAstDeclaration() ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch389_7) instanceof tom.engine.adt.tomdeclaration.types.declaration.ImplementDecl) ) {

            extendsName =  tomMatch389_7.getExpr() .getCode();
          }}}if ( tomMatch389_end_4.isEmptyconcOption() ) {tomMatch389_end_4=(( tom.engine.adt.tomoption.types.OptionList )ol);} else {tomMatch389_end_4= tomMatch389_end_4.getTailconcOption() ;}}} while(!( (tomMatch389_end_4==(( tom.engine.adt.tomoption.types.OptionList )ol)) ));}}}}

        result.add( tom.engine.adt.tomdeclaration.types.declaration.ResolveClassDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(resolveStringName) , tom___stypename, tom___ttypename, extendsName) );
      }

        //build the `ResolveXY(src,"dst");
        BQTerm bqterm =  tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.BQAppl.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(resolveStringName) , line, fileName) , tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.ModuleName.make("default") , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ) ,  tom.engine.adt.tomname.types.tomname.Name.make(resolveStringName) ,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom___sname) , line, fileName) , tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.ModuleName.make("default") , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ) ,  tom.engine.adt.tomname.types.tomname.Name.make(tom___sname) ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , "unknown type",  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) 
                        ) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) , tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make("\""+ (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getTarget() +"\"") ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) 
                , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) 









;
        return  tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction.make(bqterm) ;
      }}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_GenResolveElement( TransformerPlugin  t0,  java.util.HashSet  t1,  java.util.List  t2,  java.util.HashSet  t3,  java.util.HashSet  t4) { return new GenResolveElement(t0,t1,t2,t3,t4);}




//GenResolveElement

  private Declaration genResolveGetSlotDecl(String astName, String slotName, int line, String fileName, TomType type) {
    return  tom.engine.adt.tomdeclaration.types.declaration.ResolveGetSlotDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(astName) ,  tom.engine.adt.tomname.types.tomname.Name.make(slotName) ,  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("t") , line, fileName) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("t") , type) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("get_slot") , line, fileName) 
        ) 








;
  }

  private PairNameDecl genPairNameDecl(String slotName, Declaration slotDecl) {
    return  tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(slotName) , slotDecl) ;
  }

  private PairNameDecl genResolvePairNameDecl(String slotName, String astName, int line, String fileName, TomType type) {
    return  tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(slotName) ,  tom.engine.adt.tomdeclaration.types.declaration.ResolveGetSlotDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(astName) ,  tom.engine.adt.tomname.types.tomname.Name.make(slotName) ,  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("t") , line, fileName) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("t") , type) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("get_slot") , line, fileName) 
          ) 
        ) 











;
  }

  private String checkAndNormalizeFileName(String fileName, String prefix,
      Option orgTrack) throws TomException {
    //first, normalize file name: spaces and separators
    fileName = fileName.trim();
    fileName = fileName.replace('\\',File.separatorChar);
    fileName = fileName.replace('/',File.separatorChar);

    File file = new File(fileName);
    //isFile(): what is a "normal" file? ("not a directory" + which other
    //condition?)
    if(file.exists() && file.isFile()) {
      try {
        return file.getCanonicalPath();
      } catch (IOException e) {
        System.out.println("IO Exception when computing "+prefix+" metamodel file "+fileName);
        e.printStackTrace();
      }
    } else {
      throw new TomException(TomMessage.mmFileNotFound, new Object[]{prefix,
          fileName, ((OriginTracking)orgTrack).getFileName(),
          Integer.valueOf(((OriginTracking)orgTrack).getLine()),
          ((OriginTracking)orgTrack).getFileName()});
    }
    //well, nothing better?
    return null;
  }

}//class
